package aloe.edutrack.domain.attendance;

import aloe.edutrack.domain.lesson.Lesson;
import aloe.edutrack.domain.student.Student;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "attendances",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_attendances_student_lesson",
                columnNames = {"student_id", "lesson_id"}
        )
)
@Getter
@Setter
@NoArgsConstructor
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "lesson_id",
            nullable = false
    )
    private Lesson lesson;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "student_id",
            nullable = false
    )
    private Student student;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AttendanceStatus status;

    public Attendance(
            Lesson lesson,
            Student student,
            AttendanceStatus status
    ) {
        this.lesson = lesson;
        this.student = student;
        this.status = status;
    }
}