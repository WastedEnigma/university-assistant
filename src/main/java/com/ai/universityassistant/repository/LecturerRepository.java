package com.ai.universityassistant.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ai.universityassistant.dto.AvgDepartmentSalaryDTO;
import com.ai.universityassistant.entity.Lecturer;

public interface LecturerRepository extends JpaRepository<Lecturer, Long> {

    @Query("SELECT AVG(l.salary) " +
           "FROM Lecturer l JOIN l.departments d " +
           "WHERE d.name = :deptName ")
    Optional<BigDecimal> getAverageSalaryByDepartment(@Param("deptName") String deptName);

    @Query("SELECT new com.ai.universityassistant.dto.AvgDepartmentSalaryDTO(d.name, AVG(l.salary)) " +
           "FROM Lecturer l JOIN l.departments d " +
           "GROUP BY d.name")
    List<AvgDepartmentSalaryDTO> getAverageSalaryByDepartments();
}
