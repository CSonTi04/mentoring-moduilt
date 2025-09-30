package training.mentoringmodulith.courses.adapter.controller;

import training.mentoringmodulith.courses.application.inboundport.AnnouncementRequest;
import training.mentoringmodulith.courses.application.inboundport.CourseDto;
import training.mentoringmodulith.courses.application.inboundport.CourseQueryService;
import training.mentoringmodulith.courses.application.inboundport.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/courses")
//inbound adater és port nincsen szétválsztva
//az adott osztály interfésze az osztály public metódusai
public class CourseController {

    private final CourseService serivce;

    private final CourseQueryService queryService;
    //exception nem rest specifikus, így ne tegyünk az exception-re http statuszt
    //az application layer-ből jön a request, az ok, kintról befelé lehet hivatkozni
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createCourse(@RequestBody AnnouncementRequest request) {
        serivce.announceCourse(request);
    }


    //más útvonalon megy, akarjuk-e keverni?
    //CQRS -> Command Query mehet teljesen más technológiával
    //5-nél több select esetén gondoljuk át -> hibernate logok alapján
    //elastic search, de már a mongo db is már jó lehet szöveges keresésre
    //így egy mentés mehet az elastic-ba és a postgresql-be is, keresés meg az elastic-ben
    //Command és query ág között gyakran használnak event-et
    //Command-ot és query-t külön szolgáltatásban/alkalmazásban is megvalósíthatjuk, így külön skálázhatók -> majdhogynem a végtelenségig
    //a kettőt egy kafka kötheti össze
    //query ágon akár lehet, hogy képernyőként vannak a táblák, mint egy data warehouse-ban
    //így a join-t megpróbálhatjuk, updatet viszont akkor mindenre kell futtatni
    //ezt hívják denormalizációnak, vagy csillag architektúra, ami a data warehouse-okban jellemző | OLAP
    //az architektúra nem kőbe vésett, készüljünk fel arra, hogy módosítható legyen
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CourseDto> findAll() {
        return queryService.findAll();
    }
}
