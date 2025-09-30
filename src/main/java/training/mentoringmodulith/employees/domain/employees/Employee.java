package training.mentoringmodulith.employees.domain.employees;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class Employee {

    @Getter
    private EmployeeId id;

    @Getter
    private String name;

}
