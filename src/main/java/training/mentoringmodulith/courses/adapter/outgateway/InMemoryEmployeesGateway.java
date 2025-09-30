package training.mentoringmodulith.courses.adapter.outgateway;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import training.mentoringmodulith.courses.application.outboundport.gateway.EmployeesGateway;
import training.mentoringmodulith.courses.domain.enrollments.EmployeeId;
import training.mentoringmodulith.employees.application.inbound.EmployeeApplicationService;

@Service
@RequiredArgsConstructor
public class InMemoryEmployeesGateway implements EmployeesGateway {

    private final EmployeeApplicationService employeeApplicationService;

    @Override
    public boolean existsById(EmployeeId employeeId) {
        //lehetne csak egy olyan metódus is, hogy boolean existsById(long id), de így is jó
        //attól függ, mizu a másik csapatnál
        return employeeApplicationService.findEmployeeById(employeeId.value()).isPresent();
    }
}
