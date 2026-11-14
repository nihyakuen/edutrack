package aloe.edutrack.domain.attendance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    @Query("""
        select a from Attendance a
        where a.lesson.id = :lessonId
          and a.student.id in :studentIds
    """)
    List<Attendance> findAllByLessonIdAndStudentIds(
            @Param("lessonId") Long lessonId,
            @Param("studentIds") Collection<Long> studentIds
    );

    @Query("""
        select a
        from Attendance a
        where a.student.id = :studentId
          and a.lesson.id in :lessonIds
    """)
    List<Attendance> findAllByStudentIdAndLessonIds(
            @Param("studentId") Long studentId,
            @Param("lessonIds") Collection<Long> lessonIds
    );
}
