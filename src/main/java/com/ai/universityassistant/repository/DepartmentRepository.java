package com.ai.universityassistant.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ai.universityassistant.entity.Department;
import com.ai.universityassistant.repository.projection.DepartmentStatsProjection;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

    @Query(value = """
            SELECT d.name AS name,
            l_head.full_name AS headName,
            (SELECT COUNT(s.id) FROM students s WHERE s.department_id = d.id) AS studentCount,
            (SELECT AVG(l.salary) FROM lecturers l
                JOIN lecturer_departments ld ON l.id = ld.lecturer_id
                WHERE ld.department_id = d.id) AS avgSalary
            FROM departments d
            LEFT JOIN lecturers l_head ON d.head_id = l_head.id
            WHERE LOWER(d.name) LIKE LOWER(CONCAT('%', :name, '%'))
            LIMIT 1
            """, nativeQuery = true)
    Optional<DepartmentStatsProjection> findAggregatedStats(@Param("name") String name);
}
