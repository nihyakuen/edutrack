package aloe.edutrack.domain.student.dto;

import aloe.edutrack.domain.subjectassignment.Semester;

public record StudentSubjectResponse(
        Long assignmentId,
        Long subjectId,
        String subjectCode,
        String subjectName,
        int academicYear,
        Semester semester
) {}
