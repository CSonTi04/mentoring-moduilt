package training.mentoringmodulith.courses.application;


import training.mentoringmodulith.courses.application.inboundport.AnnouncementRequest;
import training.mentoringmodulith.courses.application.inboundport.CourseService;
import training.mentoringmodulith.courses.application.inboundport.EnrollmentRequest;
import training.mentoringmodulith.courses.application.usecase.AnnouncementUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import training.mentoringmodulith.courses.application.usecase.EmployeeHasLeavedUseCase;
import training.mentoringmodulith.courses.application.usecase.EnrollmentUseCase;
import training.mentoringmodulith.courses.domain.enrollments.EmployeeId;

//ez már nem aggrregate-enként van, ez többet is magába foglalhat
//domain service nem lesz, Java se meg tudja csinálni több entitás esetén, főleg számolások
//ez már használhat Bean validation-t, Mapstruct-ot, hívhat repositorít, fogadhat hívásokat controllertől, használhat springet, di-t etc-etc
@Service
@RequiredArgsConstructor
public class CourseApplicationService implements CourseService {

    //itt code string legyen vagy CourseCode?
    //metódus paramétereket hízlalni nem jó, ezért érdemes DTO-t használni
    //ez a controller api, entitás nem hagyhatja el a service réteget, nem lehet semminek a parmaétere vagy visszatérési rétege
    //DDD és api first ellentmondásban van
    //minden jó elv azt mondja, hogy középpen van az üzleti logika, ami körülötte van az implementation details
    //az openApi leíró fájlból generált elemek az lehet az application service interface-en, de alatti rétegekben már átmappelés
    //Jtech blog, gondolatok modellezésőrl, DTO -> lásd Full Mapping, pl Rest / Soap api közösítése

    private final AnnouncementUseCase announcementUseCase;

    private final EnrollmentUseCase enrollmentUseCase;

    private final EmployeeHasLeavedUseCase employeeHasLeavedUseCase;

    //külsőkkel tartja a kapcsolatot, de az entitásban van az üzleti logika
    //kell majd egy repository interface, ami egy port lesz, azaz egy outbound adapter
    //tesztelőknek jó lehet, ha itt visszatérünk valami azonosítóval

    @Override
    public void announce(AnnouncementRequest request) {
        announcementUseCase.announceCourse(request);
    }

    @Override
    public void enroll(EnrollmentRequest request) {
        enrollmentUseCase.enroll(request);
    }

    @Override
    public void employeeHasLeaved(long employeeId) {
        employeeHasLeavedUseCase.leave(new EmployeeId(employeeId));
    }
}
