package aloe.edutrack.domain.grade;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface GradeRepository extends JpaRepository<Grade, Long> {

    @Query("""
        select g from Grade g
        where g.assessment.id = :assessmentId
          and g.student.id in :studentIds
    """)
    List<Grade> findAllByAssessmentIdAndStudentId(
            @Param("assessmentId") Long assessmentId,
            @Param("studentIds") Collection<Long> studentIds
    );

    Optional<Grade> findByAssessmentIdAndStudentId(
            Long assessmentId,
            Long studentId
    );

    @Query("""
    select g
    from Grade g
    where g.student.id = :studentId
      and g.assessment.id in :assessmentIds
    """)
    List<Grade> findAllByStudentIdAndAssessmentIds(
            @Param("studentId") Long studentId,
            @Param("assessmentIds") Collection<Long> assessmentIds
    );
}
