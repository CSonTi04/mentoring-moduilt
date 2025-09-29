package com.training.mentoringmoduilt.courses.domain.courses;

public record CourseCode(String code) {

    //compact vs canonical constructor
    //defensive programming?? clean code esetén mi legyen, csak a service-ben állítsunk dogokat?
    //DDD azt mondja, hogy ez itt kell ellenőrizni, mert konzisztenciát it kell megtartani
    public CourseCode {
        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("Course code cannot be null or blank");
        }
    }
}
