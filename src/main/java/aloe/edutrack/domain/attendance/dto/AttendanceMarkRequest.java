package aloe.edutrack.domain.attendance.dto;

import aloe.edutrack.domain.attendance.AttendanceStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record AttendanceMarkRequest(
        @NotNull
        @Positive
        Long studentId,

        @NotNull
        AttendanceStatus status
) {
}
