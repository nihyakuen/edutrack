package aloe.edutrack.domain.subjectassignment;

import aloe.edutrack.domain.group.StudyGroup;
import aloe.edutrack.domain.subject.Subject;
import aloe.edutrack.domain.user.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "subject_assignments",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_subject_assignments",
                columnNames = {
                        "subject_id",
                        "group_id",
                        "academic_year",
                        "semester"
                }
        )
)
@Getter
@Setter
@NoArgsConstructor
public class SubjectAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "subject_id",
            nullable = false
    )
    private Subject subject;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "group_id",
            nullable = false
    )
    private StudyGroup group;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "teacher_id",
            nullable = false
    )
    private User teacher;

    @Column(name = "academic_year", nullable = false)
    private int academicYear;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Semester semester;

    public SubjectAssignment(
            Subject subject,
            StudyGroup group,
            User teacher,
            int academicYear,
            Semester semester
    ) {
        this.subject = subject;
        this.group = group;
        this.teacher = teacher;
        this.academicYear = academicYear;
        this.semester = semester;
    }
}