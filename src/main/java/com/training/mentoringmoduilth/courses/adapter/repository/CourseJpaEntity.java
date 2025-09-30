package com.training.mentoringmoduilth.courses.adapter.repository;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//Nem működik az attribute converterrel
//törekedjünk, hogy 5-7 legyen max a fieldek száma
//vannak olyan attribútumok, amik befolyásolják az üzleti működést, az entity-k tartsák meg az üzleti fieldeket, a többi legyen embeddable
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
//Lombok @Data nem jó,
public class CourseJpaEntity {
    @Id
    private String code;

    private String title;
}
