package training.mentoringmodulith.employees.adapters.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import training.mentoringmodulith.employees.application.outbound.repo.EmployeeRepository;
import training.mentoringmodulith.employees.domain.employees.Employee;
import training.mentoringmodulith.employees.application.inbound.EmployeeDto;
import training.mentoringmodulith.employees.domain.employees.EmployeeId;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class DelegatingEmployeeRepository implements EmployeeRepository {

    private final EmployeeJpaEntityRepository delegate;

    @Override
    public Employee save(Employee employee) {
        return toDomain(delegate.save(toEntity(employee)));
    }

    private Employee toDomain(EmployeeJpaEntity entity) {
        return new Employee(new EmployeeId(entity.getId()), entity.getName());
    }

    @Override
    public List<EmployeeDto> findAll() {
        return delegate.findAllDto();
    }

    @Override
    public Optional<EmployeeDto> findDtoById(EmployeeId employeeId) {
        return delegate.findDtoById(employeeId.value());
    }

    @Override
    public void deleteById(EmployeeId employeeId) {
        delegate.deleteById(employeeId.value());
    }

    private EmployeeJpaEntity toEntity(Employee employee) {
        return new EmployeeJpaEntity(employee.getName());
    }

}
