package aloe.edutrack.domain.group;

import aloe.edutrack.domain.group.dto.StudyGroupCreateRequest;
import aloe.edutrack.domain.group.dto.StudyGroupResponse;
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
import org.springframework.web.bind.annotation.*;

@Tag(name = "Study groups", description = "Управление учебными группами")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/groups")
public class StudyGroupController {
    private final StudyGroupService studyGroupService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Создать группу") // admin
    public StudyGroupResponse create(@Valid @RequestBody StudyGroupCreateRequest request) {
        return studyGroupService.create(request);
    }

    @GetMapping
    @Operation(summary = "Получить список групп")
    public Page<StudyGroupResponse> getAllStudyGroups(
            @RequestParam(defaultValue = "0")
            @Min(0) int page,

            @RequestParam(defaultValue = "30")
            @Min(1) @Max(100) int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return studyGroupService.getAllStudyGroups(pageable);
    }
}
