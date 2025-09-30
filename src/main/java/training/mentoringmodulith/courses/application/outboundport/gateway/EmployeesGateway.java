package training.mentoringmodulith.courses.application.outboundport.gateway;

import training.mentoringmodulith.courses.domain.enrollments.EmployeeId;

public interface EmployeesGateway {

    //sajátjában évő employee id-t fog kapni és visszaadja hogy létezik-e ilyen id-jű employee
    boolean existsById(EmployeeId employeeId);
}
