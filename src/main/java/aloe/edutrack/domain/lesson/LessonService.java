package aloe.edutrack.domain.lesson;

import aloe.edutrack.domain.lesson.dto.LessonCreateRequest;
import aloe.edutrack.domain.lesson.dto.LessonResponse;
import aloe.edutrack.domain.subjectassignment.SubjectAssignment;
import aloe.edutrack.domain.subjectassignment.SubjectAssignmentRepository;
import aloe.edutrack.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LessonService {
    private final LessonRepository lessonRepository;
    private final SubjectAssignmentRepository subjectAssignmentRepository;

    @Transactional
    public LessonResponse create(LessonCreateRequest request, Long currentUserId) {
        SubjectAssignment assignment = subjectAssignmentRepository
                .findById(request.subjectAssignmentId())
                .orElseThrow(() -> new ResourceNotFoundException(
                                "Subject assignment", request.subjectAssignmentId()
                ));

        if (!assignment.getTeacher().getId().equals(currentUserId)) {
            throw new AccessDeniedException(
                    "Assignment does not belong to current teacher"
            );
        }

        Lesson lesson = new Lesson(
                assignment,
                request.lessonDate(),
                request.topic().trim()
        );

        Lesson saved = lessonRepository.save(lesson);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public Page<LessonResponse> findAllBySubjectAssignmentId(Long subjectAssignmentId, Long currentUserId, Pageable pageable) {
        SubjectAssignment assignment = subjectAssignmentRepository
                .findById(subjectAssignmentId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Subject assignment", subjectAssignmentId
                ));

        if (!assignment.getTeacher().getId().equals(currentUserId)) {
            throw new AccessDeniedException(
                    "Assignment does not belong to current teacher"
            );
        }

        Page<Lesson> lessons = lessonRepository.findAllBySubjectAssignmentId(subjectAssignmentId, pageable);
        return lessons.map(this::toResponse);
    }

    private LessonResponse toResponse(Lesson lesson) {
        return new LessonResponse(
                lesson.getId(),
                lesson.getSubjectAssignment().getId(),
                lesson.getLessonDate(),
                lesson.getTopic()
        );
    }

}
