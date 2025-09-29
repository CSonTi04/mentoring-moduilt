package com.training.mentoringmoduilt.courses.application.usecase;

import com.training.mentoringmoduilt.courses.application.AnnouncementRequest;
import com.training.mentoringmoduilt.courses.application.outboundport.CourseRepository;
import com.training.mentoringmoduilt.courses.domain.courses.Course;
import com.training.mentoringmoduilt.courses.domain.courses.CourseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AnnnouncementUseCase {

    private final CourseRepository courseRepository;

    public void announceCourse(AnnouncementRequest request) {
        var code = new CourseCode(request.code());
        if(courseRepository.existsWithCode(code)){
            throw new IllegalArgumentException("Course with code %s already exists".formatted(request.code()));
        }
        var course = Course.announce(new CourseCode(request.code()), request.title());
        courseRepository.save(course);
    }
}
