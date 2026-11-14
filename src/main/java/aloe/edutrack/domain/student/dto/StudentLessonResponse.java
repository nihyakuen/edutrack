package aloe.edutrack.domain.student.dto;

import aloe.edutrack.domain.attendance.AttendanceStatus;

import java.time.LocalDate;

public record StudentLessonResponse(
        Long lessonId,
        LocalDate lessonDate,
        String topic,
        AttendanceStatus status
) {}
