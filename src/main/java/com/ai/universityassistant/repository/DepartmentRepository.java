package com.ai.universityassistant.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ai.universityassistant.entity.Department;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

    // Find department by name for tool-calling
    @Query("SELECT d FROM Department d WHERE LOWER(d.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    Optional<Department> findByNameFuzzy(@Param("name") String name);
}
