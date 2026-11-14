package aloe.edutrack.domain.subjectassignment.dto;

import aloe.edutrack.domain.subjectassignment.Semester;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record SubjectAssignmentCreateRequest(

        @NotNull
        Long subjectId,

        @NotNull
        Long groupId,

        @NotNull
        Long teacherId,

        @Positive
        int academicYear,

        @NotNull
        Semester semester

) {}
