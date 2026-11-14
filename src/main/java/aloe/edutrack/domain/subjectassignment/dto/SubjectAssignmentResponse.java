package aloe.edutrack.domain.subjectassignment.dto;

import aloe.edutrack.domain.subjectassignment.Semester;

public record SubjectAssignmentResponse(
        Long id,

        Long subjectId,
        String subjectCode,
        String subjectName,

        Long groupId,
        String groupCode,

        Long teacherId,
        String teacherName,

        int academicYear,
        Semester semester
) {}
