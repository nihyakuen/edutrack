package aloe.edutrack.domain.student;

import aloe.edutrack.domain.assessment.Assessment;
import aloe.edutrack.domain.assessment.AssessmentRepository;
import aloe.edutrack.domain.attendance.Attendance;
import aloe.edutrack.domain.attendance.AttendanceRepository;
import aloe.edutrack.domain.grade.Grade;
import aloe.edutrack.domain.grade.GradeRepository;
import aloe.edutrack.domain.student.dto.StudentLessonResponse;
import aloe.edutrack.domain.group.StudyGroup;
import aloe.edutrack.domain.group.StudyGroupRepository;
import aloe.edutrack.domain.lesson.Lesson;
import aloe.edutrack.domain.lesson.LessonRepository;
import aloe.edutrack.domain.student.dto.StudentAssessmentResponse;
import aloe.edutrack.domain.student.dto.StudentCreateRequest;
import aloe.edutrack.domain.student.dto.StudentResponse;
import aloe.edutrack.domain.student.dto.StudentSubjectResponse;
import aloe.edutrack.domain.subject.Subject;
import aloe.edutrack.domain.subjectassignment.SubjectAssignment;
import aloe.edutrack.domain.subjectassignment.SubjectAssignmentRepository;
import aloe.edutrack.domain.user.User;
import aloe.edutrack.domain.user.UserRepository;
import aloe.edutrack.domain.user.UserRole;
import aloe.edutrack.exception.BusinessRuleException;
import aloe.edutrack.exception.ResourceAlreadyExistsException;
import aloe.edutrack.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final StudyGroupRepository studyGroupRepository;
    private final SubjectAssignmentRepository subjectAssignmentRepository;
    private final AssessmentRepository assessmentRepository;
    private final GradeRepository gradeRepository;
    private final LessonRepository lessonRepository;
    private final AttendanceRepository attendanceRepository;

    @Transactional
    public StudentResponse create(StudentCreateRequest request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new ResourceNotFoundException("User", request.userId()));

        if (user.getRole() != UserRole.STUDENT) {
            throw new BusinessRuleException("User must have Student role");
        }

        if (studentRepository.existsByUserId(user.getId())) {
            throw new ResourceAlreadyExistsException("Student already exists: " + user.getId());
        }

        StudyGroup group = studyGroupRepository.findById(request.groupId())
                .orElseThrow(() -> new ResourceNotFoundException("Study group", request.groupId()));

        Student student = new Student(user, group);
        Student saved = studentRepository.save(student);

        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public Page<StudentResponse> findAllStudents(Long groupId, Pageable pageable) {
        Page<Student> students = groupId == null
                ? studentRepository.findAll(pageable)
                : studentRepository.findAllByGroupId(groupId, pageable);

        return students.map(this::toResponse);
    }

    private StudentResponse toResponse(Student student) {
        return new StudentResponse(
                student.getId(),
                student.getUser().getId(),
                student.getUser().getEmail(),
                student.getUser().getFirstName(),
                student.getUser().getLastName(),
                student.getGroup().getId(),
                student.getGroup().getCode()
        );
    }

    @Transactional(readOnly = true)
    public List<StudentSubjectResponse> getSubjects(Long currentUserId) {
        Student student = studentRepository.findByUserId(currentUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Student profile not found", currentUserId));

        Long groupId = student.getGroup().getId();

        List<SubjectAssignment> assignments =
                subjectAssignmentRepository.findAllByGroupIdWithSubject(groupId);

        return assignments.stream()
                .map(assignment -> {
                    Subject subject = assignment.getSubject();

                    return new StudentSubjectResponse(
                            assignment.getId(),
                            subject.getId(),
                            subject.getCode(),
                            subject.getName(),
                            assignment.getAcademicYear(),
                            assignment.getSemester()
                    );
                })
                .toList();
    }

    @Transactional(readOnly = true)
    public Page<StudentAssessmentResponse> getAssessmentsForStudent(
            Long assignmentId,
            Long currentUserId,
            Pageable pageable
    ) {
        Student student = getStudentWithAccessToAssignment(assignmentId, currentUserId);
        Page<Assessment> assessments = assessmentRepository.findAllBySubjectAssignmentId(assignmentId, pageable);

        List<Long> assessmentIds = assessments.getContent().stream()
                .map(Assessment::getId)
                .toList();

        Map<Long, Grade> gradesByAssessmentId = assessmentIds.isEmpty()
                ? Map.of()
                : gradeRepository
                .findAllByStudentIdAndAssessmentIds(
                        student.getId(),
                        assessmentIds
                )
                .stream()
                .collect(Collectors.toMap(
                        grade -> grade.getAssessment().getId(),
                        Function.identity()
                ));

        return assessments.map(assessment -> {
            Grade grade = gradesByAssessmentId.get(assessment.getId());

            return new StudentAssessmentResponse(
                    assessment.getId(),
                    assessment.getTitle(),
                    assessment.getType(),
                    assessment.getAssessmentDate(),
                    assessment.getMaxScore(),
                    grade == null ? null : grade.getScore()
            );
        });
    }

    @Transactional(readOnly = true)
    public Page<StudentLessonResponse> getLessonsForStudent(
            Long assignmentId,
            Long currentUserId,
            Pageable pageable
    ) {
        Student student = getStudentWithAccessToAssignment(assignmentId, currentUserId);
        Page<Lesson> lessons = lessonRepository.findAllBySubjectAssignmentId(assignmentId, pageable);

        List<Long> lessonIds = lessons.getContent().stream()
                .map(Lesson::getId)
                .toList();

        Map<Long, Attendance> attendanceByLessonId = lessonIds.isEmpty()
                ? Map.of()
                : attendanceRepository
                .findAllByStudentIdAndLessonIds(
                        student.getId(),
                        lessonIds
                )
                .stream()
                .collect(Collectors.toMap(
                        attendance -> attendance.getLesson().getId(),
                        Function.identity()
                ));

        return lessons.map(lesson -> {
            Attendance attendance =
                    attendanceByLessonId.get(lesson.getId());

            return new StudentLessonResponse(
                    lesson.getId(),
                    lesson.getLessonDate(),
                    lesson.getTopic(),
                    attendance == null ? null : attendance.getStatus()
            );
        });
    }

    private Student getStudentWithAccessToAssignment(
            Long assignmentId,
            Long currentUserId
    ) {
        Student student = studentRepository.findByUserId(currentUserId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Student profile", currentUserId
                ));

        SubjectAssignment assignment = subjectAssignmentRepository
                .findById(assignmentId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "SubjectAssignment", assignmentId
                ));

        if (!student.getGroup().getId()
                .equals(assignment.getGroup().getId())) {
            throw new AccessDeniedException(
                    "Student does not belong to assignment group"
            );
        }

        return student;
    }
}
