package training.mentoringmodulith.courses.adapter.repository;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

//Nem működik az attribute converterrel
//törekedjünk, hogy 5-7 legyen max a fieldek száma
//vannak olyan attribútumok, amik befolyásolják az üzleti működést, az entity-k tartsák meg az üzleti fieldeket, a többi legyen embeddable
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@Table(name = "courses")
//Lombok @Data nem jó,
public class CourseJpaEntity {
    @Id
    private String code;

    private String title;

    @Column(name = "headcount")
    private int limit;

    @OneToMany(mappedBy = "course")
    private List<EnrollmentJpaEntity> enrollments = new ArrayList<>();
}
