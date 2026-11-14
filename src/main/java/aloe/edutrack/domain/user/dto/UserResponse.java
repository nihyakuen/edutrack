package aloe.edutrack.domain.user.dto;

import aloe.edutrack.domain.user.UserRole;
import aloe.edutrack.security.CustomUserDetails;

public record UserResponse(
        Long id,
        String email,
        String firstName,
        String lastName,
        UserRole role,
        boolean enabled
) {
    public static UserResponse from(CustomUserDetails details) {
        return new UserResponse(
                details.getId(),
                details.getEmail(),
                details.getFirstName(),
                details.getLastName(),
                details.getRole(),
                details.isEnabled()
        );
    }
}