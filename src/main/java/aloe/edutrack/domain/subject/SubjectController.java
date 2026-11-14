package aloe.edutrack.domain.subject;

import aloe.edutrack.domain.subject.dto.SubjectCreateRequest;
import aloe.edutrack.domain.subject.dto.SubjectResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "Subjects",
        description = "Управление предметами"
)
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/subjects")
@Validated
public class SubjectController {
    private final SubjectService subjectService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Создать предмет")
    public SubjectResponse create( // admin @PreAuthorize("hasRole('ADMIN')")
            @Valid @RequestBody SubjectCreateRequest request) {
        return subjectService.create(request);
    }

    @GetMapping
    @Operation(summary = "Получить список предметов")
    public Page<SubjectResponse> getAllSubjects(
            @RequestParam(defaultValue = "0")
            @Min(0) int page,

            @RequestParam(defaultValue = "30")
            @Min(1) @Max(100) int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return subjectService.getAllSubjects(pageable);
    }
}
