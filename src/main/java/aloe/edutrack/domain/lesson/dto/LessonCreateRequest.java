package aloe.edutrack.domain.lesson.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record LessonCreateRequest(
        @NotNull
        Long subjectAssignmentId,

        @NotNull
        LocalDate lessonDate,

        @NotBlank
        @Size(max = 255)
        String topic

) {}