package aloe.edutrack.domain.attendance;

import aloe.edutrack.domain.attendance.dto.AttendanceBatchRequest;
import aloe.edutrack.domain.attendance.dto.AttendanceMarkRequest;
import aloe.edutrack.domain.attendance.dto.AttendanceRowResponse;
import aloe.edutrack.domain.lesson.Lesson;
import aloe.edutrack.domain.lesson.LessonRepository;
import aloe.edutrack.domain.student.Student;
import aloe.edutrack.domain.student.StudentRepository;
import aloe.edutrack.domain.subjectassignment.SubjectAssignment;
import aloe.edutrack.domain.user.User;
import aloe.edutrack.exception.BusinessRuleException;
import aloe.edutrack.exception.ResourceNotFoundException;
import aloe.edutrack.security.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttendanceService {
    private final AttendanceRepository attendanceRepository;
    private final LessonRepository lessonRepository;
    private final StudentRepository studentRepository;

    @Transactional(readOnly = true)
    public Page<AttendanceRowResponse> getAttendanceByLessonId(Long lessonId, Long currentUserId, Pageable pageable) {
        Lesson lesson = getOwnedLesson(lessonId, currentUserId);
        Long groupId = lesson.getSubjectAssignment().getGroup().getId();

        Page<Student> students = studentRepository.findPageByGroupWithUser(groupId, pageable);
        List<Long> studentIds = students.getContent().stream()
                .map(Student::getId)
                .toList();

        Map<Long, Attendance> attendanceByStudentId = studentIds.isEmpty() ? Map.of() : attendanceRepository
                .findAllByLessonIdAndStudentIds(lessonId, studentIds)
                .stream()
                .collect(Collectors.toMap(
                        attendance -> attendance.getStudent().getId(),
                        Function.identity()
                ));

        return students.map(student -> {
            Attendance attendance = attendanceByStudentId.get(student.getId());

            User user = student.getUser();
            return new AttendanceRowResponse(
                    student.getId(),
                    user.getFirstName(),
                    user.getLastName(),
                    attendance == null ? null : attendance.getId(),
                    attendance == null ? null : attendance.getStatus()
            );
        });
    }

    @Transactional
    public void patchAttendances(Long lessonId, @Valid AttendanceBatchRequest request, Long currentUserId) {
        Lesson lesson = getOwnedLesson(lessonId, currentUserId);
        Long groupId = lesson.getSubjectAssignment().getGroup().getId();

        Set<Long> studentIds = new HashSet<>();
        for (AttendanceMarkRequest record : request.records()) {
            if (!studentIds.add(record.studentId())) {
                throw new BusinessRuleException(
                        "Duplicate student ID: " + record.studentId()
                );
            }
        }

        List<Student> students = studentRepository.findAllByIdsAndGroupId(studentIds, groupId);

        if (students.size() != studentIds.size()) {
            throw new BusinessRuleException(
                    "One or more students do not belong to lesson group"
            );
        }

        Map<Long, Student> studentsById = students.stream()
                .collect(Collectors.toMap(
                        Student::getId,
                        Function.identity()
                ));

        Map<Long, Attendance> existing = attendanceRepository
                .findAllByLessonIdAndStudentIds(lessonId, studentIds)
                .stream()
                .collect(Collectors.toMap(
                        a -> a.getStudent().getId(),
                        Function.identity()
                ));

        List<Attendance> newAttendances = new ArrayList<>();

        for (AttendanceMarkRequest record : request.records()) {

            Attendance attendance = existing.get(record.studentId());

            if (attendance == null) {
                newAttendances.add(new Attendance(
                        lesson,
                        studentsById.get(record.studentId()),
                        record.status()
                ));
            } else {
                attendance.setStatus(record.status());
            }
        }

        attendanceRepository.saveAll(newAttendances);
    }

    private Lesson getOwnedLesson(Long lessonId, Long currentUserId) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Lesson", lessonId
                ));

        SubjectAssignment assignment = lesson.getSubjectAssignment();

        if (!assignment.getTeacher().getId().equals(currentUserId)) {
            throw new AccessDeniedException(
                    "Lesson does not belong to current teacher"
            );
        }

        return lesson;
    }
}
