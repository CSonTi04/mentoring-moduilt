package training.mentoringmodulith.courses.application.inboundport;

import jakarta.validation.constraints.NotNull;

//az egyik path param lenne a másik a body-ban jönne
//de ez nem tudja, hogy rest lesz-e vagy soap
public record EnrollmentRequest(
        @NotNull long employeeId,
        @NotNull String courseCode
) {
}
