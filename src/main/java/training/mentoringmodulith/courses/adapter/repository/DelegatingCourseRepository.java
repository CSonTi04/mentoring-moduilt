package training.mentoringmodulith.courses.adapter.repository;

import training.mentoringmodulith.courses.application.inboundport.CourseDto;
import training.mentoringmodulith.courses.application.outboundport.repo.CourseRepository;
import training.mentoringmodulith.courses.domain.enrollments.Course;
import training.mentoringmodulith.courses.domain.enrollments.CourseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

//Itt menne a delegálás JPA repository felé, de az jpa entity-t vár, de akkor itt mappelni kell, hogy ki lehessen cserélni a mögöttes implementációt
//sok kicsit, de nem bonyolult építőkocka
//Bob bácsi - COURS alapján, vagy az együtt mozgó dolgok legyenek egy csomagban
//ez a repository egy adapter, ami a CourseRepository portot valósítja meg
@Repository
@RequiredArgsConstructor
public class DelegatingCourseRepository implements CourseRepository {

    private final CourseJpaRepository jpaRepository;

    private final CourseMapper courseMapper;

    @Override
    public void save(Course course) {
        //kézzel vissazfele referenciák bekötése -> ezt mjad mapstructtal meg kell csinálni
        var jpa = courseMapper.toEntity(course);
        for (var enrollment : jpa.getEnrollments()) {
            enrollment.setCourse(jpa);
        }
        jpaRepository.save(jpa);
    }

    @Override
    public boolean existsWithCode(CourseCode code) {
        return jpaRepository.existsById(code.code());
    }

    @Override
    public List<CourseDto> findAll() {
        return jpaRepository.findAllDto();
    }

    //ide már lehet kellene a mapstruct is?
    //lett is
    @Override
    public Course findById(CourseCode courseCode) {
        var entity = jpaRepository.findByIdWithEnrollments(courseCode.code());
        return courseMapper.toDomain(entity);
    }
}
