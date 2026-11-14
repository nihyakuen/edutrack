package aloe.edutrack.domain.grade;

import aloe.edutrack.domain.assessment.Assessment;
import aloe.edutrack.domain.student.Student;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "grades",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_grades_student_assessment",
                        columnNames = {
                                "student_id",
                                "assessment_id"
                        }
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
public class Grade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "student_id",
            nullable = false
    )
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "assessment_id",
            nullable = false
    )
    private Assessment assessment;

    @Column(nullable = false)
    private int score;

    public Grade(
            Student student,
            Assessment assessment,
            int score
    ) {
        this.student = student;
        this.assessment = assessment;
        this.score = score;
    }
}
