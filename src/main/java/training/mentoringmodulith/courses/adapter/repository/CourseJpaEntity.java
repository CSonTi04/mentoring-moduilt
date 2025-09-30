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

    @Column(name = "enrollment_limit")
    private int limit;

    //oprhan removal - ha a listából kiveszem, akkor törlődik az adatbázisból is
    //cascade all - ha a course törlődik, akkor az összes enrollment is -> ez szerintem risky
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EnrollmentJpaEntity> enrollments = new ArrayList<>();
}
