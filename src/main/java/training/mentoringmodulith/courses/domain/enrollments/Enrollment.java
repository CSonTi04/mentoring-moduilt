package training.mentoringmodulith.courses.domain.enrollments;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class Enrollment {
    @NonNull
    private EmployeeId employeeId;
    @NotNull
    private LocalDateTime enrollmentDate;
}
