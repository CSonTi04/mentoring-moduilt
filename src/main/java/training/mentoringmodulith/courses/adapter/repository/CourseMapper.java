package training.mentoringmodulith.courses.adapter.repository;

import org.mapstruct.Mapper;
import training.mentoringmodulith.courses.domain.enrollments.Course;
import training.mentoringmodulith.courses.domain.enrollments.CourseCode;
import training.mentoringmodulith.courses.domain.enrollments.EmployeeId;
import training.mentoringmodulith.courses.domain.enrollments.Enrollment;

@Mapper(componentModel = "spring")
public interface CourseMapper {

    default String mapCourseCOde(CourseCode courseCode) {
        return courseCode.code();
    }

    default Long mapEmployeeId(EmployeeId id) {
        return id.value();
    }

    default EmployeeId mapToEmployeeId(Long id) {
        return new EmployeeId(id);
    }

    default Course toDomain(CourseJpaEntity entity) {
        if (entity == null) {
            return null;
        }

        // Assuming Course has a factory method or constructor that takes these parameters
        // Adjust the parameters according to the actual Course creation method
        return Course.announce(
                new CourseCode(entity.getCode()),
                entity.getTitle(),
                entity.getLimit()
        );
    }

    CourseJpaEntity toEntity(Course course);

    Enrollment toDomain(EnrollmentJpaEntity entity);
}
