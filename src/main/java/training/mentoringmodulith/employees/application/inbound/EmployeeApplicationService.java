package training.mentoringmodulith.employees.application.inbound;

import training.mentoringmodulith.employees.domain.employees.EmployeeId;

import java.util.List;
import java.util.Optional;

public interface EmployeeApplicationService {

    EmployeeDto join(EmployeeDto employee);

    void leave(EmployeeId employeeId);

    List<EmployeeDto> findAll();

    Optional<EmployeeDto> findEmployeeById(long employeeId);
}
