package com.training.mentoringmoduilt.courses.adapter.repository;

import com.training.mentoringmoduilt.courses.application.outboundport.CourseRepository;
import com.training.mentoringmoduilt.courses.domain.courses.Course;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

//Itt menne a delegálás JPA repository felé, de az jpa entity-t vár, de akkor itt mappelni kell, hogy ki lehessen cserélni a mögöttes implementációt
//sok kicsit, de nem bonyolult építőkocka
//Bob bácsi - COURS alapján, vagy az együtt mozgó dolgok legyenek egy csomagban
//ez a repository egy adapter, ami a CourseRepository portot valósítja meg
@Repository
@RequiredArgsConstructor
public class DelegatingCourseRepository implements CourseRepository {

    private final CourseJpaRepository jpaRepository;

    @Override
    public void save(Course course) {
        var japEntity = new CourseJpaEntity(course.getCode().code(), course.getTitle());
        jpaRepository.save(japEntity);
    }
}
