package com.training.mentoringmoduilth.courses.application.outboundport;

import com.training.mentoringmoduilth.courses.domain.courses.Course;
import com.training.mentoringmoduilth.courses.domain.courses.CourseCode;

public interface CourseRepository {

    void save(Course courseEntity);

    boolean existsWithCode(CourseCode code);
}
