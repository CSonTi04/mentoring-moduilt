package training.mentoringmodulith.employees.application;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import training.mentoringmodulith.employees.application.inbound.EmployeeApplicationService;
import training.mentoringmodulith.employees.application.outbound.gateway.EmployeeHasLeaved;
import training.mentoringmodulith.employees.application.outbound.repo.EmployeeRepository;
import training.mentoringmodulith.employees.domain.employees.Employee;
import training.mentoringmodulith.employees.domain.employees.EmployeeId;
import training.mentoringmodulith.employees.application.inbound.EmployeeDto;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
//Spring-ben ban event kezelés alapból
public class DelegatingEmployeeApplicationService implements EmployeeApplicationService {

    private final EmployeeRepository repository;

    private final EmployeeMapper mapper;

    private final ApplicationEventPublisher eventPublisher;

    @Override
    public EmployeeDto join(EmployeeDto employee) {
        Employee entity = mapper.toEntity(employee);
        entity = repository.save(entity);
        return mapper.toDto(entity);
    }

    @Override
    public void leave(EmployeeId employeeId) {
        repository.deleteById(employeeId);
        eventPublisher.publishEvent(new EmployeeHasLeaved(employeeId.value()));
    }

    @Override
    public List<EmployeeDto> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<EmployeeDto> findEmployeeById(long employeeId) {
        return repository.findDtoById(new EmployeeId(employeeId));
    }
}
