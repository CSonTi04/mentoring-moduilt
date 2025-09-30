package training.mentoringmoduilth.courses.adapter.repository;

import training.mentoringmoduilth.courses.application.outboundport.CourseRepository;
import training.mentoringmoduilth.courses.domain.courses.Course;
import training.mentoringmoduilth.courses.domain.courses.CourseCode;
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
        var jpaEntity = new CourseJpaEntity(course.getCode().code(), course.getTitle());
        jpaRepository.save(jpaEntity);
    }

    @Override
    public boolean existsWithCode(CourseCode code) {
        return jpaRepository.existsById(code.code());
    }
}
