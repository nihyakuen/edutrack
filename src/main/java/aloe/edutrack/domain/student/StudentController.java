package aloe.edutrack.domain.student;

import aloe.edutrack.domain.student.dto.StudentLessonResponse;
import aloe.edutrack.domain.student.dto.StudentAssessmentResponse;
import aloe.edutrack.domain.student.dto.StudentCreateRequest;
import aloe.edutrack.domain.student.dto.StudentResponse;
import aloe.edutrack.domain.student.dto.StudentSubjectResponse;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Students",
        description = "Управление студентами"
)
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/students")
public class StudentController {
    private final StudentService studentService;

    @Operation(summary = "Создать студента")
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StudentResponse create(@Valid @RequestBody StudentCreateRequest request) {
        return studentService.create(request);
    }

    @Operation(
            summary = "Получить список студентов",
            description = """
                    Возвращает страницу студентов.
                    Поддерживает необязательную фильтрацию по groupId.
                    Результаты сортируются по ID студента.
                    """)
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public Page<StudentResponse> getAllStudents(
            @RequestParam(required = false) Long groupId,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "30") @Min(1) @Max(100) int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        return studentService.findAllStudents(groupId, pageable);
    }

    @Operation(summary = "Получить дисциплины студента")
    @GetMapping("/me/subjects")
    @PreAuthorize("hasAuthority('STUDENT')")
    public List<StudentSubjectResponse> getSubjects(
            @AuthenticationPrincipal CustomUserDetails currentUser
    ) {
        return studentService.getSubjects(currentUser.getId());
    }

    @Operation(summary = "Получить у студента задания и оценки по дисциплине")
    @GetMapping("/me/subject-assignments/{assignmentId}/assessments")
    @PreAuthorize("hasAuthority('STUDENT')")
    public Page<StudentAssessmentResponse> getAssessments(
            @PathVariable Long assignmentId,
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "30") @Min(1) @Max(100) int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        return studentService.getAssessmentsForStudent(
                assignmentId,
                currentUser.getId(),
                pageable
        );
    }

    @Operation(summary = "Получить занятия и посещаемость по дисциплине")
    @GetMapping("/me/subject-assignments/{assignmentId}/lessons")
    @PreAuthorize("hasAuthority('STUDENT')")
    public Page<StudentLessonResponse> getLessons(
            @PathVariable Long assignmentId,
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "30") @Min(1) @Max(100) int size
    ) {
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by("id").ascending()
        );

        return studentService.getLessonsForStudent(
                assignmentId,
                currentUser.getId(),
                pageable
        );
    }
}
