package aloe.edutrack.domain.grade;

import aloe.edutrack.domain.grade.dto.GradeResponse;

public record GradeUpsertResult(
        GradeResponse grade,
        boolean created
) {}