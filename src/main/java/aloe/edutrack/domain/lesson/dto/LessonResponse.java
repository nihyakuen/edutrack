package aloe.edutrack.domain.lesson.dto;

import java.time.LocalDate;

public record LessonResponse(
        Long id,
        Long subjectAssignmentId,
        LocalDate lessonDate,
        String topic
) {}