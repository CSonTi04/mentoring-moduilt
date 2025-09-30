package training.mentoringmodulith.employees.application;

import org.mapstruct.Mapper;
import training.mentoringmodulith.employees.application.inbound.EmployeeDto;
import training.mentoringmodulith.employees.domain.employees.Employee;
import training.mentoringmodulith.employees.domain.employees.EmployeeId;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    default Long map(EmployeeId value) {
        return value == null ? null : value.value();
    }

    default EmployeeId map(Long value) {
        return value == null ? null : new EmployeeId(value);
    }

    EmployeeDto toDto(Employee employee);

    Employee toEntity(EmployeeDto employeeDto);
}
