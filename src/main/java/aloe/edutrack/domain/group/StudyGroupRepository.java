package aloe.edutrack.domain.group;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyGroupRepository extends JpaRepository<StudyGroup, Long> {
    boolean existsByCode(String code);
}
