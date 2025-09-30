package training.mentoringmodulith.courses.application.outboundport;

import training.mentoringmodulith.courses.application.inboundport.CourseDto;
import training.mentoringmodulith.courses.domain.enrollments.Course;
import training.mentoringmodulith.courses.domain.enrollments.CourseCode;

import java.util.List;

public interface CourseRepository {

    void save(Course courseEntity);

    boolean existsWithCode(CourseCode code);

    List<CourseDto> findAll();
}
