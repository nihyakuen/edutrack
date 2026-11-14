package aloe.edutrack.domain.grade.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record GradeUpsertRequest(
        @NotNull
        @PositiveOrZero
        Integer score
) {}
