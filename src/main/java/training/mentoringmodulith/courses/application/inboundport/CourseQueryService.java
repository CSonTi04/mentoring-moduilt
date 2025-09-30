package training.mentoringmodulith.courses.application.inboundport;

import java.util.List;


//Clean code-ban benne van, hogy ne legyen hungarian notation, mert nem tudjuk, hogy mikor lesz belőle implementáció
//Interface-t nem azért használjuk, mert több implementáció lesz, hanem mert le kell választani
public interface CourseQueryService {

    //kurzusok listázása
    //entitást nem engedhetjük ki az application rétegből, így dto-t kell csinálni

    List<CourseDto> findAll();
}
