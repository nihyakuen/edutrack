package aloe.edutrack.domain.grade.dto;

public record GradeResponse (
        Long id,
        Long studentId,
        Long assessmentId,
        int score
) {
}
