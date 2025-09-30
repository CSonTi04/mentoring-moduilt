package training.mentoringmodulith.employees.adapters.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import training.mentoringmodulith.employees.application.inbound.EmployeeDto;

import java.util.List;
import java.util.Optional;

public interface EmployeeJpaEntityRepository extends JpaRepository<EmployeeJpaEntity, Long> {
    @Query("""
        select new training.mentoringmodulith.employees.application.inbound.EmployeeDto(
        e.id, 
        e.name) 
        from EmployeeJpaEntity e order by e.name
""")
    List<EmployeeDto> findAllDto();

    @Query("""
        select new training.mentoringmodulith.employees.application.inbound.EmployeeDto(
        e.id, 
        e.name) 
        from EmployeeJpaEntity e where e.id = :id
""")
    Optional<EmployeeDto> findDtoById(long id);
}
