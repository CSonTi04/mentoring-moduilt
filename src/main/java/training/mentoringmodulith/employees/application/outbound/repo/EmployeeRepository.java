package training.mentoringmodulith.employees.application.outbound.repo;

import training.mentoringmodulith.employees.domain.employees.Employee;
import training.mentoringmodulith.employees.domain.employees.EmployeeId;
import training.mentoringmodulith.employees.application.inbound.EmployeeDto;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository {

    Employee save(Employee employee);

    List<EmployeeDto> findAll();

    Optional<EmployeeDto> findDtoById(EmployeeId employeeId);

    void deleteById(EmployeeId employeeId);
}
