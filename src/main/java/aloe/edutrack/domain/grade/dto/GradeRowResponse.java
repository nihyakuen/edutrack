package aloe.edutrack.domain.grade.dto;

public record GradeRowResponse(
        Long studentId,
        String firstName,
        String lastName,
        Long gradeId,
        Integer score
) {}
