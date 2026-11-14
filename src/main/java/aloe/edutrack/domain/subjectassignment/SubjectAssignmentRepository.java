package aloe.edutrack.domain.subjectassignment;

import org.jspecify.annotations.NullMarked;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

@NullMarked
public interface SubjectAssignmentRepository extends JpaRepository<SubjectAssignment, Long> {

    @Query("""
        select case when count(sa) > 0 then true else false end
        from SubjectAssignment sa
        where sa.subject.id = :subjectId
          and sa.group.id = :groupId
          and sa.academicYear = :academicYear
          and sa.semester = :semester
    """)
    boolean existsAssignment(
            @Param("subjectId") Long subjectId,
            @Param("groupId") Long groupId,
            @Param("academicYear") int academicYear,
            @Param("semester") Semester semester
    );

    @EntityGraph(attributePaths = {"subject", "group", "teacher"})
    Optional<SubjectAssignment> findWithDetailsById(Long id);

    @Override
    @EntityGraph(attributePaths = {"subject", "group", "teacher"})
    Page<SubjectAssignment> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"subject", "group", "teacher"})
    Page<SubjectAssignment> findAllByGroupId(Long groupId, Pageable pageable);

    @EntityGraph(attributePaths = {"subject", "group", "teacher"})
    Page<SubjectAssignment> findAllByTeacherId(Long teacherId, Pageable pageable);

    @EntityGraph(attributePaths = {"subject", "group", "teacher"})
    Page<SubjectAssignment> findAllByTeacherIdAndGroupId(Long teacherId, Long groupId, Pageable pageable);

    @Query("""
    select sa from SubjectAssignment sa
    join fetch sa.subject
    where sa.group.id = :groupId
    order by sa.id
    """)
    List<SubjectAssignment> findAllByGroupIdWithSubject(@Param("groupId") Long groupId);
}
