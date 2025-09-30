package com.training.mentoringmoduilth.courses.adapter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
//ez az interface - port
public interface CourseJpaRepository extends JpaRepository<CourseJpaEntity, String> {
}
