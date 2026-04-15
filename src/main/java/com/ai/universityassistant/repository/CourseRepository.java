package com.ai.universityassistant.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ai.universityassistant.entity.Course;
import com.ai.universityassistant.repository.projection.CourseProjection;

public interface CourseRepository extends JpaRepository<Course, Long> {

    @Query(value = """
            SELECT c.title AS title,c.credits AS credits,
            COALESCE(STRING_AGG(s.full_name, ', '), '') AS studentNames
            FROM courses c
            JOIN course_lecturers cl ON c.id = cl.course_id
            JOIN lecturers l ON cl.lecturer_id = l.id
            JOIN degrees d ON l.degree_id = d.id AND d.title = 'Professor'
            LEFT JOIN enrollments e ON c.id = e.course_id
            LEFT JOIN students s ON e.student_id = s.id
            WHERE l.full_name ILIKE CONCAT('%', :professorName, '%')
            GROUP BY c.id, c.title, c.credits
                    """, nativeQuery = true)
    List<CourseProjection> findAllByProfessorName(@Param("professorName") String professorName);
}
