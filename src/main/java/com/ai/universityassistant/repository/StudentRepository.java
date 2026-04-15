package com.ai.universityassistant.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ai.universityassistant.entity.Student;
import com.ai.universityassistant.repository.projection.CourseGradeProjection;
import com.ai.universityassistant.repository.projection.PersonSearchProjection;
import com.ai.universityassistant.repository.projection.StudentProjection;

public interface StudentRepository extends JpaRepository<Student, Long> {

        @Query(value = """
                        SELECT full_name AS name, 'LECTURER' AS role, 'Multiple' AS departmentName
                        FROM lecturers
                        WHERE to_tsvector('simple', full_name) @@ websearch_to_tsquery('simple', :query)
                        UNION ALL
                        SELECT s.full_name AS name, 'STUDENT' AS role, d.name AS departmentName
                        FROM students s
                        JOIN departments d ON s.department_id = d.id
                        WHERE to_tsvector('simple', s.full_name) @@ websearch_to_tsquery('simple', :query)
                        """, nativeQuery = true)
        List<PersonSearchProjection> searchAll(@Param("query") String query);

        @Query(value = """
                        SELECT c.title as courseTitle, e.grade as grade
                        FROM courses c
                        JOIN enrollments e ON c.id = e.course_id
                        WHERE e.student_id = :studentId
                        """, nativeQuery = true)
        List<CourseGradeProjection> findGradesByStudentId(@Param("studentId") Long studentId);

        // window function example
        @Query(value = """
                        WITH RankedStudents AS (
                                SELECT
                                s.full_name AS fullName,
                                d.name AS departmentName,
                                e.grade AS gpa,
                                ROW_NUMBER() OVER (
                                        PARTITION BY c.id
                                        ORDER BY e.grade DESC
                                ) as rankNumber
                                FROM enrollments e
                                JOIN students s ON e.student_id = s.id
                                JOIN departments d ON s.department_id = d.id
                                JOIN courses c ON e.course_id = c.id
                                WHERE c.title ILIKE CONCAT(:courseTitle, '%')
                        )
                        SELECT fullName, departmentName, gpa, rankNumber
                        FROM RankedStudents
                        WHERE rankNumber <= :limit
                        ORDER BY gpa DESC
                """, nativeQuery = true)
        List<StudentProjection> findTopByCourse(
                @Param("courseTitle") String courseTitle, 
                @Param("limit") int limit);
}
