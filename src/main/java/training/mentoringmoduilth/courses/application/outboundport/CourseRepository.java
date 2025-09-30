package training.mentoringmoduilth.courses.application.outboundport;

import training.mentoringmoduilth.courses.domain.courses.Course;
import training.mentoringmoduilth.courses.domain.courses.CourseCode;

public interface CourseRepository {

    void save(Course courseEntity);

    boolean existsWithCode(CourseCode code);
}
