package aloe.edutrack.domain.lesson;

import aloe.edutrack.domain.subjectassignment.SubjectAssignment;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "lessons")
@Getter
@Setter
@NoArgsConstructor
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "subject_assignment_id",
            nullable = false
    )
    private SubjectAssignment subjectAssignment;

    @Column(name = "lesson_date", nullable = false)
    private LocalDate lessonDate;

    @Column(nullable = false)
    private String topic;

    public Lesson(
            SubjectAssignment subjectAssignment,
            LocalDate lessonDate,
            String topic
    ) {
        this.subjectAssignment = subjectAssignment;
        this.lessonDate = lessonDate;
        this.topic = topic;
    }
}
