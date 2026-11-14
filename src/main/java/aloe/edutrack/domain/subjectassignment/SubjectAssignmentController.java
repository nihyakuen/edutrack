package aloe.edutrack.domain.subjectassignment;

import aloe.edutrack.domain.assessment.AssessmentService;
import aloe.edutrack.domain.assessment.dto.AssessmentResponse;
import aloe.edutrack.domain.lesson.LessonService;
import aloe.edutrack.domain.lesson.dto.LessonResponse;
import aloe.edutrack.domain.subjectassignment.dto.SubjectAssignmentCreateRequest;
import aloe.edutrack.domain.subjectassignment.dto.SubjectAssignmentResponse;
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
        name = "Subject assignments",
        description = "Управление назначениями «дисциплина — группа — преподаватель»"
)
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/subject-assignments")
public class SubjectAssignmentController {
    private final SubjectAssignmentService subjectAssignmentService;
    private final LessonService lessonService;
    private final AssessmentService assessmentService;

    @Operation(summary = "Создать Назначение дисциплины группе")
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SubjectAssignmentResponse create(@Valid @RequestBody SubjectAssignmentCreateRequest request) {
        return subjectAssignmentService.create(request);
    }

    @Operation(summary = "Получить список назначений преподавателей",
            description = """
                    Возвращает страницу назначений преподавателей на учебные предметы и группы.
            
                    Доступ зависит от роли пользователя:
                    - ADMIN — все назначения.
                    - TEACHER — только собственные назначения.
            
                    Параметр groupId позволяет дополнительно отфильтровать назначения
                    по учебной группе. Если не указан, фильтрация по группе не применяется.
            
                    Для преподавателя идентификатор определяется автоматически
                    из данных аутентифицированного пользователя.
                    """)
    @PreAuthorize("hasAnyAuthority('ADMIN', 'TEACHER')")
    @GetMapping
    public Page<SubjectAssignmentResponse> getAllSubjectAssignments(
            @RequestParam(required = false) Long groupId,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "30") @Min(1) @Max(100) int size,
            @AuthenticationPrincipal CustomUserDetails currentUser
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());

        return subjectAssignmentService.findAllSubjectAssignments(
                groupId, pageable, currentUser.getId(), currentUser.getRole()
        );
    }

    @PreAuthorize("hasAuthority('ADMIN')") // teacher + только свои
    @Operation(summary = "Получить по id")
    @GetMapping("/{id}")
    public ResponseEntity<SubjectAssignmentResponse> getById(@PathVariable Long id) {
        SubjectAssignmentResponse response = subjectAssignmentService.getSubjectAssignmentById(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Получить список занятий текущего назначения")
    @PreAuthorize("hasAuthority('TEACHER')")
    @GetMapping("/{id}/lessons")
    public Page<LessonResponse> getAllLessonsBySubjectAssignmentId(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size,
            @AuthenticationPrincipal CustomUserDetails currentUser
    ) {
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by("lessonDate").ascending().and(Sort.by("id").ascending())
        );

        return lessonService.findAllBySubjectAssignmentId(id, currentUser.getId(), pageable);
    }

    @Operation(summary = "Получить список работ текущего назначения")
    @PreAuthorize("hasAuthority('TEACHER')")
    @GetMapping("/{id}/assessments")
    public Page<AssessmentResponse> getAllAssessmentsBySubjectAssignmentId(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size,
            @AuthenticationPrincipal CustomUserDetails currentUser
    ) {
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by("assessmentDate").ascending().and(Sort.by("id").ascending())
        );
        return assessmentService.findAllBySubjectAssignmentId(id, currentUser.getId(), pageable);
    }
}
