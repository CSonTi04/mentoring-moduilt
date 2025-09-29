package com.training.mentoringmoduilt.courses.application;

import com.training.mentoringmoduilt.courses.domain.courses.Course;
public interface CourseRepository {

    void save(Course courseEntity);
}
