package com.training.mentoringmoduilt.courses.domain.courses;

import lombok.Getter;

import java.util.Objects;

@Getter
public class Course {
    //azért nem final, mert az entitást lehet módosítani, de üzleti döntés kell, hogy mi módosítható

    //azért nincs setter, mert mondjuk egy cím esetén az lehetne move, mint költözés, vagy fix mint javítás

    //2003-ban nem volt még immutability, de most már van, így ott kérdéses, hogy mi legyen -> az inkább functional programming
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
