package aloe.edutrack.domain.group.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record StudyGroupCreateRequest(
        @NotBlank
        @Size(max = 50)
        String code
) {}
