package training.mentoringmodulith.courses.application.outboundport;

import training.mentoringmodulith.courses.domain.courses.Course;
import training.mentoringmodulith.courses.domain.courses.CourseCode;

public interface CourseRepository {

    void save(Course courseEntity);

    boolean existsWithCode(CourseCode code);
}
