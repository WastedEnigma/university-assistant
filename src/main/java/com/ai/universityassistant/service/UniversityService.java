package com.ai.universityassistant.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ai.universityassistant.dto.AvgDepartmentSalaryDTO;
import com.ai.universityassistant.dto.CourseDTO;
import com.ai.universityassistant.dto.CourseGradeDTO;
import com.ai.universityassistant.dto.DepartmentStatsDTO;
import com.ai.universityassistant.dto.PersonSearchDTO;
import com.ai.universityassistant.dto.StudentDTO;
import com.ai.universityassistant.exception.DataNotFoundException;
import com.ai.universityassistant.repository.CourseRepository;
import com.ai.universityassistant.repository.DepartmentRepository;
import com.ai.universityassistant.repository.LecturerRepository;
import com.ai.universityassistant.repository.StudentRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true) // signals that methods strictly query data and will never modify the DB state
public class UniversityService {

    private final LecturerRepository lecturerRepository;
    private final StudentRepository studentRepository;
    private final DepartmentRepository departmentRepository;
    private final CourseRepository courseRepository;

    // Tool 1: getDepartmentStats
    public DepartmentStatsDTO getDepartmentStats(String deptName) {
        log.info("AI called getDepartmentStats with: {}", deptName);

        var aggregatedStats = departmentRepository.findAggregatedStats(deptName)
                    .orElseThrow(() -> new DataNotFoundException("Aggregated stats not found"));

        log.info("Head of department: {}", aggregatedStats.getHeadName());
        log.info("Student count: {}", aggregatedStats.getStudentCount());
        log.info("Avg salary: {}", aggregatedStats.getAvgSalary());            
        
        return DepartmentStatsDTO.of(aggregatedStats);
    }

    // Tool 2: searchPeople
    public List<PersonSearchDTO> searchPeople(String query) {
        log.info("AI called searchPeople with: {}", query);

        var searchResult = studentRepository.searchAll(query);

        log.info("Database returned {} search items", searchResult.size());

        return searchResult.stream()
                .map(PersonSearchDTO::of)
                .toList();
    }

    // Tool 3: getTopStudents
    public List<StudentDTO> getTopStudents(String course, int limit) {
        log.info("AI called getTopStudents with: {} and limit: {}", course, limit);

        var topStudents = studentRepository.findTopByCourse(course,limit);

        log.info("Database returned {} students.", topStudents.size());

        return topStudents.stream()
                .map(StudentDTO::of)
                .toList();
    }

    // Tool 4: getCoursesByProfessor
    public List<CourseDTO> getCoursesByProfessor(String professorName) {
        log.info("AI called getCoursesByProfessor with professor name: {}", professorName);

        var coursesByProfessor = courseRepository.findAllByProfessorName(professorName);

        log.info("Database returned {} courses.", coursesByProfessor.size());

        return coursesByProfessor.stream()
                .map(CourseDTO::of)
                .toList();
    }

    // Tool 5: getStudentGrades
    public List<CourseGradeDTO> getStudentGrades(Long studentId) {
        log.info("AI called getStudentGrades with student ID: {}", studentId);

        if (!studentRepository.existsById(studentId)) {
            throw new DataNotFoundException("User not found");
        }

        var studentGrades = studentRepository.findGradesByStudentId(studentId);

        log.info("Database returned {} student grades.", studentGrades.size());

        return studentGrades.stream()
                .map(CourseGradeDTO::of)
                .toList();
    }

    // Tool 6: getAverageSalariesByDepartments
    public List<AvgDepartmentSalaryDTO> getAverageSalariesByDepartments() {
        log.info("AI called getAverageSalariesByDepartments");

        var averageSalaryByDepartments = lecturerRepository.getAverageSalaryByDepartments();

        log.info("Database returned {} results.", averageSalaryByDepartments.size());

        return lecturerRepository.getAverageSalaryByDepartments();
    }
}
