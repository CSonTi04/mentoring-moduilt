package com.training.mentoringmoduilt.courses.application;


import com.training.mentoringmoduilt.courses.application.outboundport.CourseRepository;
import com.training.mentoringmoduilt.courses.domain.courses.Course;
import com.training.mentoringmoduilt.courses.domain.courses.CourseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

//ez már nem aggrregate-enként van, ez többet is magába foglalhat
//domain service nem lesz, Java se meg tudja csinálni több entitás esetén, főleg számolások
//ez már használhat Bean validation-t, Mapstruct-ot, hívhat repositorít, fogadhat hívásokat controllertől, használhat springet, di-t etc-etc
@Service
@RequiredArgsConstructor
public class CourseApplicationService implements com.training.mentoringmoduilt.courses.application.inboundport.CourseService {

    //itt code string legyen vagy CourseCode?
    //metódus paramétereket hízlalni nem jó, ezért érdemes DTO-t használni
    //ez a controller api, entitás nem hagyhatja el a service réteget, nem lehet semminek a parmaétere vagy visszatérési rétege
    //DDD és api first ellentmondásban van
    //minden jó elv azt mondja, hogy középpen van az üzleti logika, ami körülötte van az implementation details
    //az openApi leíró fájlból generált elemek az lehet az application service interface-en, de alatti rétegekben már átmappelés
    //Jtech blog, gondolatok modellezésőrl, DTO -> lásd Full Mapping, pl Rest / Soap api közösítése

    private final CourseRepository courseRepository;

    //külsőkkel tartja a kapcsolatot, de az entitásban van az üzleti logika
    //kell majd egy repository interface, ami egy port lesz, azaz egy outbound adapter
    //tesztelőknek jó lehet, ha itt visszatérünk valami azonosítóval

    //TODO: kellene validáció, hogy ne lehessen duplikáció az id-n
    //TODO: kódnak nem szabadna egy karakteresnek lennie
    @Override
    public void announceCourse(AnnouncementRequest request) {
        var course = Course.announce(new CourseCode(request.code()), request.title());
        courseRepository.save(course);
    }
}
