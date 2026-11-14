package aloe.edutrack.domain.lesson;

import aloe.edutrack.domain.attendance.AttendanceService;
import aloe.edutrack.domain.attendance.dto.AttendanceBatchRequest;
import aloe.edutrack.domain.attendance.dto.AttendanceResponse;
import aloe.edutrack.domain.attendance.dto.AttendanceRowResponse;
import aloe.edutrack.domain.grade.dto.GradeUpsertRequest;
import aloe.edutrack.domain.lesson.dto.LessonCreateRequest;
import aloe.edutrack.domain.lesson.dto.LessonResponse;
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

@Tag(name = "Lessons", description = "Управление занятиями")
@RestController
@RequestMapping("/api/lessons")
@RequiredArgsConstructor
public class LessonController {
    private final LessonService lessonService;
    private final AttendanceService attendanceService;

    @Operation(summary = "Создать Занятие")
    @PreAuthorize("hasAuthority('TEACHER')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LessonResponse create(
            @Valid @RequestBody LessonCreateRequest request,
            @AuthenticationPrincipal CustomUserDetails currentUser
    ) {
        return lessonService.create(request, currentUser.getId());
    }

    @Operation(summary = "Журнал посещаемости занятия")
    @PreAuthorize("hasAuthority('TEACHER')")
    @GetMapping("/{lessonId}/attendance")
    public Page<AttendanceRowResponse> getAttendances(
            @PathVariable Long lessonId,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "30") @Min(1) @Max(100) int size,
            @AuthenticationPrincipal CustomUserDetails currentUser
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        return attendanceService.getAttendanceByLessonId(lessonId, currentUser.getId(), pageable);
    }

    @Operation(summary = "Сохранить отметки посещаемости")
    @PreAuthorize("hasAuthority('TEACHER')")
    @PatchMapping("/{lessonId}/attendance")
    public ResponseEntity<Void> patchAttendances(
            @PathVariable Long lessonId,
            @Valid @RequestBody AttendanceBatchRequest request,
            @AuthenticationPrincipal CustomUserDetails currentUser
    ) {
        attendanceService.patchAttendances(lessonId, request, currentUser.getId());

        return ResponseEntity.noContent().build();
    }
}
