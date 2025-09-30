package training.mentoringmodulith.courses.application.inboundport;

public interface CourseService {
    //külsőkkel tartja a kapcsolatot, de az entitásban van az üzleti logika
    //kell majd egy repository interface, ami egy port lesz, azaz egy outbound adapter
    //tesztelőknek jó lehet, ha itt visszatérünk valami azonosítóval
    void announceCourse(AnnouncementRequest request);
}
