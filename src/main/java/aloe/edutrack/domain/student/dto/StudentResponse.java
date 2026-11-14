package aloe.edutrack.domain.student.dto;

public record StudentResponse(
        Long id,
        Long userId,
        String email,
        String firstName,
        String lastName,
        Long groupId,
        String groupCode
) {}
