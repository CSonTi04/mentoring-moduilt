package training.mentoringmodulith.courses.domain.courses;

import lombok.Getter;

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
    private final CourseCode code;

    private final String title;

    private Course(CourseCode code, String title) {
        Objects.requireNonNull(code, "Course code cannot be null");
        if (null == title || title.isBlank()) {
            throw new IllegalArgumentException("Course title cannot be null or blank");
        }
        this.code = code;
        this.title = title;
    }

    public static Course announce(CourseCode code, String title) {
        return new Course(code, title);
    }
}
