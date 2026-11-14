package aloe.edutrack.domain.grade;

import aloe.edutrack.domain.assessment.Assessment;
import aloe.edutrack.domain.assessment.AssessmentRepository;
import aloe.edutrack.domain.grade.dto.GradeResponse;
import aloe.edutrack.domain.grade.dto.GradeRowResponse;
import aloe.edutrack.domain.student.Student;
import aloe.edutrack.domain.student.StudentRepository;
import aloe.edutrack.domain.subjectassignment.SubjectAssignment;
import aloe.edutrack.domain.user.User;
import aloe.edutrack.exception.BusinessRuleException;
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
public class GradeService {
    private final GradeRepository gradeRepository;
    private final AssessmentRepository assessmentRepository;
    private final StudentRepository studentRepository;

    @Transactional(readOnly = true)
    public Page<GradeRowResponse> getGradesByAssessmentId(Long assessmentId, Long currentUserId, Pageable pageable) {
        Assessment assessment = assessmentRepository.findById(assessmentId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Assessment", assessmentId
        ));

        SubjectAssignment assignment = assessment.getSubjectAssignment();

        if (!assignment.getTeacher().getId().equals(currentUserId)) {
            throw new AccessDeniedException(
                    "Assessment does not belong to current teacher"
            );
        }

        Long groupId = assignment.getGroup().getId();

        Page<Student> students = studentRepository.findPageByGroupWithUser(groupId, pageable);

        List<Long> studentIds = students.getContent().stream()
                .map(Student::getId)
                .toList();

        Map<Long, Grade> gradesByStudentId = studentIds.isEmpty() ? Map.of() : gradeRepository
                .findAllByAssessmentIdAndStudentId(assessmentId, studentIds)
                .stream()
                .collect(Collectors.toMap(
                        grade -> grade.getStudent().getId(),
                        Function.identity()
                ));

        return students.map(student -> {
            Grade grade = gradesByStudentId.get(student.getId());
            User user = student.getUser();

            return new GradeRowResponse(
                    student.getId(),
                    user.getFirstName(),
                    user.getLastName(),
                    grade == null ? null : grade.getId(),
                    grade == null ? null : grade.getScore()
            );
        });
    }

    @Transactional
    public GradeUpsertResult upsertGrade(
            Long assessmentId,
            Long studentId,
            Integer score,
            Long currentUserId
    ) {
        Assessment assessment = assessmentRepository.findById(assessmentId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Assessment", assessmentId
                ));

        SubjectAssignment assignment = assessment.getSubjectAssignment();

        if (!assignment.getTeacher().getId().equals(currentUserId)) {
            throw new AccessDeniedException(
                    "Assessment does not belong to current teacher"
            );
        }

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Student", studentId
                ));

        if (!student.getGroup().getId().equals(assignment.getGroup().getId())) {
            throw new BusinessRuleException(
                    "Student does not belong to assessment group"
            );
        }

        if (score == null || score < 0 || score > assessment.getMaxScore()) {
            throw new BusinessRuleException(
                    "Score must be between 0 and " + assessment.getMaxScore()
            );
        }

        Grade grade = gradeRepository
                .findByAssessmentIdAndStudentId(assessmentId, studentId)
                .orElse(null);

        boolean created = grade == null;

        if (created) {
            grade = gradeRepository.save(new Grade(student, assessment, score));
        } else {
            grade.setScore(score);
        }

        GradeResponse response = new GradeResponse(
                grade.getId(),
                student.getId(),
                assessment.getId(),
                grade.getScore()
        );

        return new GradeUpsertResult(response, created);
    }
}
