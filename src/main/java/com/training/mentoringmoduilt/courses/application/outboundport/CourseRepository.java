package com.training.mentoringmoduilt.courses.application.outboundport;

import com.training.mentoringmoduilt.courses.domain.courses.Course;
import com.training.mentoringmoduilt.courses.domain.courses.CourseCode;

public interface CourseRepository {

    void save(Course courseEntity);

    boolean existsWithCode(CourseCode code);
}
