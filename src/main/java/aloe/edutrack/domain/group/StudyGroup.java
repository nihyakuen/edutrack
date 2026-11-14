package aloe.edutrack.domain.group;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "study_groups")
@Getter
@Setter
@NoArgsConstructor
public class StudyGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String code;

    public StudyGroup(String code) {
        this.code = code;
    }
}
