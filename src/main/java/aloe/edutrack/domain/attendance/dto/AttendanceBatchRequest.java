package aloe.edutrack.domain.attendance.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record AttendanceBatchRequest(
        @NotEmpty
        List<@NotNull @Valid AttendanceMarkRequest> records
) {
}
