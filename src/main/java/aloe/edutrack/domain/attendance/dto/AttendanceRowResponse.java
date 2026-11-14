package aloe.edutrack.domain.attendance.dto;

import aloe.edutrack.domain.attendance.AttendanceStatus;

public record AttendanceRowResponse(
        Long studentId,
        String firstName,
        String lastName,
        Long attendanceId,
        AttendanceStatus status
) {
}
