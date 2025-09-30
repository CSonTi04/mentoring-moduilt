package training.mentoringmodulith.courses.domain.enrollments;

import java.time.LocalDateTime;

public record EnrollmentVO(
        EmployeeId employeeId,
        LocalDateTime enrollmentDate
) {
}
