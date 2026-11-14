package aloe.edutrack.domain.assessment.dto;

import aloe.edutrack.domain.assessment.AssessmentType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record AssessmentCreateRequest (
        @NotNull
        Long subjectAssignmentId,

        @NotBlank
        @Size(max = 255)
        String title,

        @NotNull
        AssessmentType type,

        @NotNull
        LocalDate assessmentDate,

        @NotNull
        @Positive
        Integer maxScore
) {}
