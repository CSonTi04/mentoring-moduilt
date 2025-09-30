package training.mentoringmodulith.courses.domain.enrollments;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CourseTest {
    @Test
    void announce() {
        //bug, kis feature teszt először
        //meg kell adni a course nevét és kódját
        var code = new CourseCode("JAVAX-DDD");//value object
        var title = "Programozzunk DDD-vel";//entity, aggregate root
        var limit = 15;

        //BDD-ben megvan a GIVEN

        //Most jönne a WHEN, ami vagy ctr, vagy builder hívás lenne csípőből
        //Példányosításra kell visszavezetni, de nincs neve a ctr-nek
        //Menjünk static factorie-vel
        //igazából ctr is lehetne, de így az ubuiquitious language benne van a static factory-s eléképzlésben
        var course = Course.announce(code, title, limit);//német konstrutkor, PHP?

        //ha assertEquals nem elég, akkor menjünk assertJ-re
        //hamcrest lemaradt, ezért érdemes az assertJ
        assertEquals(code, course.getCode());
        assertEquals(title, course.getTitle());
        assertEquals(limit, course.getLimit());
    }


    //mit mond ki Demeter törvénye?
    //Csak a közvetlen szomszédokkal beszélhetek -> itt jön a c++-s firend?
    //kocsi függvények sértik, trainwrecks
    @Nested
    class EnrollmentsTest {
        Course course;

        @BeforeEach
        void setUp() {
            course = Course.announce(new CourseCode("JAVAX-DDD"), "Programozzunk DDD-vel", 15);
        }

        @Test
        void enroll() {
            var employeeId = new EmployeeId(10L);
            course.enroll(employeeId);
            assertThat(course.getEnrollments()).extracting(EnrollmentVO::employeeId).containsExactly(employeeId);
        }

        @Test
        void enrollSameEmployeeTwice() {
            var employeeId = new EmployeeId(10L);
            course.enroll(employeeId);
            assertThrows(IllegalStateException.class, () -> course.enroll(employeeId));
        }

        @Test
        void enrollOverLimit() {
            for (long i = 1; i <= 15; i++) {
                course.enroll(new EmployeeId(i));
            }
            var employeeId = new EmployeeId(16L);
            assertThrows(IllegalStateException.class, () -> course.enroll(employeeId));
        }
    }
}
