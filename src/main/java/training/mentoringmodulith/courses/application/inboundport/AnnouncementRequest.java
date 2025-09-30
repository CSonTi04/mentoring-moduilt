package training.mentoringmodulith.courses.application.inboundport;

import jakarta.validation.constraints.NotNull;


//DTO-k nem magukat validálják, itt már van annotációs validáció is
//String CourseCode helyett, hogy ne legyen probléma az itnerface-n
public record AnnouncementRequest(
        @NotNull String code,
        @NotNull String title
) {
}
