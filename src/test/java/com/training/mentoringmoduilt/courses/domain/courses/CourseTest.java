package com.training.mentoringmoduilt.courses.domain.courses;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CourseTest {
    @Test
    void announce() {
        //bug, kis feature teszt először
        //meg kell adni a course nevét és kódját
        var code = new CourseCode("JAVAX-DDD");//value object
        var title = "Programozzunk DDD-vel";//entity, aggregate root

        //BDD-ben megvan a GIVEN

        //Most jönne a WHEN, ami vagy ctr, vagy builder hívás lenne csípőből
        //Példányosításra kell visszavezetni, de nincs neve a ctr-nek
        //Menjünk static factorie-vel
        //igazából ctr is lehetne, de így az ubuiquitious language benne van a static factory-s eléképzlésben
        var course = Course.announce(code, title);//német konstrutkor, PHP?

        //ha assertEquals nem elég, akkor menjünk assertJ-re
        //hamcrest lemaradt, ezért érdemes az assertJ
        assertEquals(code, course.getCode());
        assertEquals(title, course.getTitle());
    }
}
