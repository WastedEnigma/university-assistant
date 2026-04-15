package com.ai.universityassistant.config;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

import com.ai.universityassistant.dto.AvgDepartmentSalaryDTO;
import com.ai.universityassistant.dto.CourseDTO;
import com.ai.universityassistant.dto.CourseGradeDTO;
import com.ai.universityassistant.dto.DepartmentStatsDTO;
import com.ai.universityassistant.dto.PersonSearchDTO;
import com.ai.universityassistant.dto.StudentDTO;
import com.ai.universityassistant.service.UniversityService;

@Configuration
public class AiConfig {

    @Bean
    @Description(AssistantConstants.Description.DEPT_STATS)
    public Function<DepartmentRequest, DepartmentStatsDTO> getDepartmentStats(UniversityService service) {
        return request -> service.getDepartmentStats(request.name());
    }

    @Bean
    @Description(AssistantConstants.Description.SEARCH_PEOPLE)
    public Function<SearchRequest, List<PersonSearchDTO>> searchPeople(UniversityService service) {
        return request -> service.searchPeople(request.query());
    }

    @Bean
    @Description(AssistantConstants.Description.TOP_STUDENTS)
    public Function<TopStudentsRequest, List<StudentDTO>> getTopStudents(UniversityService service) {
        return request -> service.getTopStudents(request.course(), request.limit());
    }

    @Bean
    @Description(AssistantConstants.Description.COURSES_PROFESSOR)
    public Function<CourseRequest, List<CourseDTO>> getCoursesByProfessorName(UniversityService service) {
        return request -> service.getCoursesByProfessor(request.professorName());
    }

    @Bean
    @Description(AssistantConstants.Description.STUDENT_GRADES)
    public Function<StudentGradesRequest, List<CourseGradeDTO>> getStudentGrades(UniversityService service) {
        return request -> service.getStudentGrades(request.studentId());
    }

    @Bean
    @Description(AssistantConstants.Description.AVG_DEPTS_SALARIES)
    public Supplier<List<AvgDepartmentSalaryDTO>> getAverageSalariesByDepartments(UniversityService service) {
        return () -> service.getAverageSalariesByDepartments();
    }

    // Records for tool inputs
    public record DepartmentRequest(String name) {}
    public record SearchRequest(String query) {}
    public record TopStudentsRequest(String course, int limit) {}
    public record CourseRequest(String professorName) {}
    public record StudentGradesRequest(Long studentId) {}
}
