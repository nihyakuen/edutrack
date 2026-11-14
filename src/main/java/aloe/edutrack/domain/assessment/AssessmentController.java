package aloe.edutrack.domain.assessment;

import aloe.edutrack.domain.assessment.dto.AssessmentCreateRequest;
import aloe.edutrack.domain.assessment.dto.AssessmentResponse;
import aloe.edutrack.domain.grade.GradeService;
import aloe.edutrack.domain.grade.dto.GradeUpsertRequest;
import aloe.edutrack.domain.grade.dto.GradeResponse;
import aloe.edutrack.domain.grade.dto.GradeRowResponse;
import aloe.edutrack.domain.grade.GradeUpsertResult;
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


@Tag(name = "Assessments")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/assessments")
public class AssessmentController {
    private final AssessmentService assessmentService;
    private final GradeService gradeService;

    @Operation(summary = "Создать задание")
    @PreAuthorize("hasAuthority('TEACHER')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AssessmentResponse create(
            @Valid @RequestBody AssessmentCreateRequest request,
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        return assessmentService.create(request, currentUser.getId());
    }

    @Operation(summary = "Получить ведомость оценок")
    @PreAuthorize("hasAuthority('TEACHER')")
    @GetMapping("/{assessmentId}/grades")
    public Page<GradeRowResponse> getGrades(
            @PathVariable Long assessmentId,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "30") @Min(1) @Max(100) int size,
            @AuthenticationPrincipal CustomUserDetails currentUser
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        return gradeService.getGradesByAssessmentId(assessmentId, currentUser.getId(), pageable);
    }

    @Operation(summary = "Поставить оценку студенту")
    @PreAuthorize("hasAuthority('TEACHER')")
    @PutMapping("/{assessmentId}/grades/{studentId}")
    public ResponseEntity<GradeResponse> upsertGrade(
            @PathVariable Long assessmentId,
            @PathVariable Long studentId,
            @Valid @RequestBody GradeUpsertRequest request,
            @AuthenticationPrincipal CustomUserDetails currentUser
    ) {
        GradeUpsertResult result = gradeService.upsertGrade(
                assessmentId,
                studentId,
                request.score(),
                currentUser.getId()
        );

        return ResponseEntity
                .status(result.created()
                        ? HttpStatus.CREATED
                        : HttpStatus.OK)
                .body(result.grade());
    }
}