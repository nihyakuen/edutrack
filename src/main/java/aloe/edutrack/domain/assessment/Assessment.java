package aloe.edutrack.domain.assessment;

import aloe.edutrack.domain.subjectassignment.SubjectAssignment;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "assessments")
@Getter
@Setter
@NoArgsConstructor
public class Assessment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "subject_assignment_id",
            nullable = false
    )
    private SubjectAssignment subjectAssignment;

    @Column(nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AssessmentType type;

    @Column(name = "assessment_date", nullable = false)
    private LocalDate assessmentDate;

    @Column(name = "max_score", nullable = false)
    private int maxScore;

    public Assessment(
            SubjectAssignment subjectAssignment,
            String title,
            AssessmentType type,
            LocalDate assessmentDate,
            int maxScore
    ) {
        this.subjectAssignment = subjectAssignment;
        this.title = title;
        this.type = type;
        this.assessmentDate = assessmentDate;
        this.maxScore = maxScore;
    }
}
