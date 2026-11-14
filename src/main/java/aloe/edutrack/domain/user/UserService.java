package aloe.edutrack.domain.user;

import aloe.edutrack.domain.user.dto.UserCreateRequest;
import aloe.edutrack.domain.user.dto.UserResponse;
import aloe.edutrack.exception.ResourceAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponse create(UserCreateRequest request) {
        String email = request.email().trim().toLowerCase(Locale.ROOT);

        if (userRepository.existsByEmail(email)) {
            throw new ResourceAlreadyExistsException("User already exists: " + email);
        }

        String passwordHash = passwordEncoder.encode(request.password());

        User user = new User(
                email,
                passwordHash,
                request.firstName().trim(),
                request.lastName().trim(),
                request.role()
        );

        User saved = userRepository.save(user);

        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public Page<UserResponse> getAllUsers(UserRole role, Pageable pageable) {
        Page<User> users = role == null
                ? userRepository.findAll(pageable)
                : userRepository.findAllByRole(role, pageable);

        return users.map(this::toResponse);
    }

    private UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getRole(),
                user.isEnabled()
        );
    }
}
