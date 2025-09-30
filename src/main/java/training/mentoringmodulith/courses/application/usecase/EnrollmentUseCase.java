package training.mentoringmodulith.courses.application.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import training.mentoringmodulith.courses.application.inboundport.EnrollmentRequest;
import training.mentoringmodulith.courses.application.outboundport.CourseRepository;
import training.mentoringmodulith.courses.domain.enrollments.CourseCode;
import training.mentoringmodulith.courses.domain.enrollments.EmployeeId;

@Component
@RequiredArgsConstructor
public class EnrollmentUseCase {

    private final CourseRepository repository;
    //application service itt, beszélhet domai servic-szel, beszélhet root aggreaget-tel, meg portokkla, de nincs benne üzleti logika
    public void enroll(EnrollmentRequest request) {
        var course = repository.findById(new CourseCode(request.courseCode()));
        course.enroll(new EmployeeId(request.employeeId()));
        repository.save(course);
    }
}
