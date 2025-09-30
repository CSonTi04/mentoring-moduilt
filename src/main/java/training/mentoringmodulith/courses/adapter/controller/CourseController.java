package training.mentoringmodulith.courses.adapter.controller;

import training.mentoringmodulith.courses.application.AnnouncementRequest;
import training.mentoringmodulith.courses.application.usecase.AnnouncementUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/courses")
//inbound adater és port nincsen szétválsztva
//az adott osztály interfésze az osztály public metódusai
public class CourseController {

    private final AnnouncementUseCase useCase;
    //exception nem rest specifikus, így ne tegyünk az exception-re http statuszt
    //az application layer-ből jön a request, az ok, kintról befelé lehet hivatkozni
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createCourse(@RequestBody AnnouncementRequest request) {
        useCase.announceCourse(request);
    }
}
