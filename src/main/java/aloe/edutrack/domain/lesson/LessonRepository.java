package aloe.edutrack.domain.lesson;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface LessonRepository extends JpaRepository<Lesson, Long> {
    Page<Lesson> findAllBySubjectAssignmentId(
            @Param("subjectAssignmentId") Long subjectAssignmentId,
            Pageable pageable
    );
}