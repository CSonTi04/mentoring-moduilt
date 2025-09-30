package training.mentoringmodulith.courses.domain.enrollments;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
public class Course {
    //azért nem final, mert az entitást lehet módosítani, de üzleti döntés kell, hogy mi módosítható

    //azért nincs setter, mert mondjuk egy cím esetén az lehetne move, mint költözés, vagy fix mint javítás

    //2003-ban nem volt még immutability, de most már van, így ott kérdéses, hogy mi legyen -> az inkább functional programming
    //ha egy entity létrehozása bonyolult akkor hasnzálj factory-t vagy builder-t -> bilder gang of four-os, de itt a builder az lehet factory is
    //gang of four factory vs DDD factory

    //DDD-t nem éri meg tesztelés nélkül használni, ert a lényeg a domain logika, nem kell semmit mockolni, mert nincs külső függőség
    //a domain logika tesztelése a lényeg, nem a frameworkök
    //még mindig nem éri meg a crud szintű témákhoz

    //ez a course csak a jelentkezés adatait reprezentálja, nem az árat, vagy a temiatikát
    //ehhez lesz egy enrollment entity
    //Enrollment -> EMployeeId, meg enrollmentDate
    private final CourseCode code;

    private final String title;

    private final int limit;

    private List<Enrollment> enrollments = new ArrayList<>();

    private Course(CourseCode code, String title, int limit) {
        Objects.requireNonNull(code, "Course code cannot be null");
        if (null == title || title.isBlank()) {
            throw new IllegalArgumentException("Course title cannot be null or blank");
        }
        if (limit <= 1) {
            //lehetne visszafelé kopmabilis is, de ez egy üzleti döntés, pl lehetne hogy limit nélküli, vagy lenne deafault limit
            //így egészen a restig kompatibilis lenne
            throw new IllegalArgumentException("Course limit must be greater than zero");
        }
        this.code = code;
        this.title = title;
        this.limit = limit;
    }

    public static Course announce(CourseCode code, String title, int limit) {
        return new Course(code, title, limit);
    }

    //nem anemic model, így könnyebb tesztelni, mert a logika egy helyen van
    //hogyan írjunk hozzá tesztet?
    //fejlesztő úgy írni a tesztesetet, hogy sorba menne
    //tesztelői elmondás alapján pozitív ággal menjünk végig
    //Demeter törvényt sértene, ha kijönne an enrollmen
    //Talán hasEnrolled vonal?
    //Enrollmnet VO-kat lehetne visszaadni
    //Státuszt is lehetne asszertálni
    //itt lehetne TDD-zni, mert nincsen külső függőség, szépen lehetne iteráltan haladni
    public EnrollmentVO enroll(EmployeeId employeeId) {
        //Course az aggregate root, ő kezeli a jelentkezéseket, a külvilág nem férhet hozzá közvetlenül az elfedett adatokhoz
        //itt nem kaphatunk Enrollmentet, mert az egy belső adat
        Objects.requireNonNull(employeeId, "EmployeeId cannot be null");

        if (enrollments.stream().anyMatch(e -> e.getEmployeeId().equals(employeeId))) {
            throw new IllegalStateException("Employee [" + employeeId + "] is already enrolled in course [" + code + "]");
        }
        if (limit == enrollments.size()) {
            throw new IllegalStateException("Course [" + code + "] is full: " + enrollments.size() + "/" + limit + " enrolled");
        }

        //Itt nem lehet mapstructot használni, kézzel kell gyúrni
        enrollments.add(new Enrollment(employeeId, LocalDateTime.now()));
        return new EnrollmentVO(employeeId, LocalDateTime.now());
    }

    public List<EnrollmentVO> getEnrollments() {
        return enrollments.stream()
                .map(e -> new EnrollmentVO(e.getEmployeeId(), e.getEnrollmentDate()))
                .toList();
    }
}
