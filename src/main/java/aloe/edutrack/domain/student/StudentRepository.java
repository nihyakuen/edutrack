package aloe.edutrack.domain.student;

import org.jspecify.annotations.NullMarked;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@NullMarked
public interface StudentRepository extends JpaRepository<Student, Long> {

    @Override
    @EntityGraph(attributePaths = {"user", "group"})
    Page<Student> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"user", "group"})
    Page<Student> findAllByGroupId(Long groupId, Pageable pageable);

    boolean existsByUserId(Long userId);

    @Query(
            value = """
        select s from Student s
        join fetch s.user
        where s.group.id = :groupId
    """,
            countQuery = """
        select count(s) from Student s
        where s.group.id = :groupId
    """)
    Page<Student> findPageByGroupWithUser(
            @Param("groupId") Long groupId,
            Pageable pageable
    );

    @Query("""
        select s from Student s
        where s.id in :studentIds
          and s.group.id = :groupId
    """)
    List<Student> findAllByIdsAndGroupId(
            @Param("studentIds") Collection<Long> studentIds,
            @Param("groupId") Long groupId
    );

    Optional<Student> findByUserId(Long userId);
}