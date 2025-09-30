package training.mentoringmodulith.employees.adapters.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import training.mentoringmodulith.employees.application.inbound.EmployeeApplicationService;
import training.mentoringmodulith.employees.application.inbound.EmployeeDto;
import training.mentoringmodulith.employees.domain.employees.EmployeeId;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/employees")
public class EmployeesController {

    private final EmployeeApplicationService employeeApplicationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EmployeeDto join(@RequestBody EmployeeDto employee) {
        return employeeApplicationService.join(employee);
    }

    @GetMapping
    public List<EmployeeDto> findAll() {
        return employeeApplicationService.findAll();
    }

    @DeleteMapping("/{id}")
    public void leave(@PathVariable long id) {
        employeeApplicationService.leave(new EmployeeId(id));
    }
}
