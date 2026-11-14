package aloe.edutrack.domain.user;

import aloe.edutrack.domain.user.dto.UserCreateRequest;
import aloe.edutrack.domain.user.dto.UserResponse;
import aloe.edutrack.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "Users",
        description = "Управление пользователями"
)
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @Operation(summary = "Создать пользователя")
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse create(@Valid @RequestBody UserCreateRequest request) {
        return userService.create(request);
    }

    @Operation(summary = "Получить список пользователей",
            description = """
                    Возвращает страницу пользователей.
                    Поддерживает необязательную фильтрацию по роли.
                    Результаты сортируются по ID пользователя.
                    case-insensitive (role= ADMIN, admin, AdMiN, aDmin и тд)
                    """)
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public Page<UserResponse> getAllUsers(
            @RequestParam(name = "role", required = false) UserRole role,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "30") @Min(1) @Max(100) int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        return userService.getAllUsers(role, pageable);
    }

    @Operation(summary = "Получить профиль текущего пользователя")
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ResponseEntity.ok(UserResponse.from(userDetails));
    }
}
