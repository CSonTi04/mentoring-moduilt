package training.mentoringmodulith.courses.adapter.repository;

import org.mapstruct.Mapper;
import training.mentoringmodulith.courses.domain.enrollments.*;

@Mapper(componentModel = "spring")
public interface CourseMapper {

    default String map(CourseCode code) {
        return code.code();
    }

    default CourseCode map(String code) {
        return new CourseCode(code);
    }

    default Long map(EmployeeId employeeId) {
        return employeeId.value();
    }

    default EmployeeId map(Long employeeId) {
        return new EmployeeId(employeeId);
    }

    CourseJpaEntity toEntity(Course course);

    Course toDomain(CourseJpaEntity entity);

    Enrollment toDomain(EnrollmentJpaEntity entity);
}