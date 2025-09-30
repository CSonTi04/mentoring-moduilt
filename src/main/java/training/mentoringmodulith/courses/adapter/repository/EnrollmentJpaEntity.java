package training.mentoringmodulith.courses.adapter.repository;

import jakarta.persistence.*;
import lombok.*;
import training.mentoringmodulith.courses.domain.enrollments.CourseCode;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter@Setter
@Table(name = "enrollments")
public class EnrollmentJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private long employeeId;

    private LocalDateTime enrollmentDate;

    @ManyToOne
    private CourseJpaEntity course;
}
