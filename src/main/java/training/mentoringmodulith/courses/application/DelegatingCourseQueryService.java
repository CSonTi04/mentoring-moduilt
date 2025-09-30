package training.mentoringmodulith.courses.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import training.mentoringmodulith.courses.application.inboundport.CourseDto;
import training.mentoringmodulith.courses.application.inboundport.CourseQueryService;
import training.mentoringmodulith.courses.application.outboundport.CourseRepository;

import java.util.List;

//Lehet ebben bonyultabb, mint csak visszaadni a deleagte eredményét?
//DTO, vagy JPA entity, vagy domain entity?
//Legyen itt a mapping?
//JPA entitás módosításra valók
//Miért van, hogy egyből JPA entitást kapunk vissza, majd egyből mapstructtal mappelünk?
//Megoldás, hogy egyből DTO-t queryzünk -> projection használata
//Nem fogja managed objectekkel szemetelni a memóriát
//Hibernate dirty checking -> 1 darab entity memóriában, egyet megkapunk a tranzaikció végén megnézi, hogy melyik property változott meg
//Ha van változás, akkor updateeli az adatbázisban
//Ha projectiont használunk, akkor nincs dirty checking, mert nem entityt kapunk vissza
//Join-ok esetén meg főleg hatákonyabb

//Fejlesztő mindig csak az összetett mondat elejét olvassa el, szereti a szimmetriát
@Service
@RequiredArgsConstructor
public class DelegatingCourseQueryService implements CourseQueryService {

    private final CourseRepository repository;

    @Override
    public List<CourseDto> findAll() {
        return repository.findAll();
    }
}
