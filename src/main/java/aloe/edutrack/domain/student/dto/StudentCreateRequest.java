package aloe.edutrack.domain.student.dto;

import jakarta.validation.constraints.NotNull;

public record StudentCreateRequest(
        @NotNull
        Long userId,

        @NotNull
        Long groupId
) {}
