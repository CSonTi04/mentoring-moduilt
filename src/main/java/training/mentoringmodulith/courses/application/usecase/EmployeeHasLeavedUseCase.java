package training.mentoringmodulith.courses.application.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import training.mentoringmodulith.courses.application.outboundport.gateway.EmployeesGateway;
import training.mentoringmodulith.courses.application.outboundport.repo.CourseRepository;
import training.mentoringmodulith.courses.domain.enrollments.CourseCode;
import training.mentoringmodulith.courses.domain.enrollments.EmployeeId;
import training.mentoringmodulith.courses.domain.services.LeaveDomainService;

@Service
@RequiredArgsConstructor
public class EmployeeHasLeavedUseCase {

    private final CourseRepository repository;

    private final EmployeesGateway employeesGateway;

    private final LeaveDomainService leaveDomainService;

    public void leave(EmployeeId employeeId) {
        //üzleti logika itt nem
        //ez nem egy aggregate, hanem egy aggregate-en belül több entitáson
        if (!employeesGateway.existsById(employeeId)) {
            throw new IllegalArgumentException("Employee with id %d does not exist".formatted(employeeId.value()));
        }
        var courses = repository.findAll();
        var coursesDomainEntities = courses.stream().map(c -> repository.findById(new CourseCode(c.code()))).toList();
        leaveDomainService.leave(coursesDomainEntities, employeeId);
        coursesDomainEntities.forEach(repository::save);
    }
}
