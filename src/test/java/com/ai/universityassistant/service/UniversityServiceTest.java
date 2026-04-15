package com.ai.universityassistant.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ai.universityassistant.dto.AvgDepartmentSalaryDTO;
import com.ai.universityassistant.dto.CourseDTO;
import com.ai.universityassistant.dto.CourseGradeDTO;
import com.ai.universityassistant.dto.DepartmentStatsDTO;
import com.ai.universityassistant.dto.PersonSearchDTO;
import com.ai.universityassistant.dto.StudentDTO;
import com.ai.universityassistant.entity.Course;
import com.ai.universityassistant.entity.Department;
import com.ai.universityassistant.entity.Lecturer;
import com.ai.universityassistant.entity.Student;
import com.ai.universityassistant.repository.CourseRepository;
import com.ai.universityassistant.repository.DepartmentRepository;
import com.ai.universityassistant.repository.LecturerRepository;
import com.ai.universityassistant.repository.StudentRepository;
import com.ai.universityassistant.repository.projection.CourseGradeProjection;
import com.ai.universityassistant.repository.projection.CourseProjection;
import com.ai.universityassistant.repository.projection.PersonSearchProjection;
import com.ai.universityassistant.repository.projection.StudentProjection;

@ExtendWith(MockitoExtension.class)
class UniversityServiceTest {

    @Mock
    private LecturerRepository lecturerRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private UniversityService universityService;

    @Test
    void testGetDepartmentStats_Success() {
        Department dept = new Department();
        dept.setName("Computer Science");
        Lecturer head = new Lecturer();
        head.setFullName("Dr. Smith");
        dept.setHead(head);

        when(departmentRepository.findByNameFuzzy("Computer Science"))
                .thenReturn(Optional.of(dept));
        when(studentRepository.countByDepartmentName("Computer Science"))
                .thenReturn(150L);
        when(lecturerRepository.getAverageSalaryByDepartment("Computer Science"))
                .thenReturn(Optional.of(new BigDecimal(75000.0)));

        DepartmentStatsDTO result = universityService.getDepartmentStats("Computer Science");

        assertNotNull(result);
        assertEquals("Computer Science", result.name());
        assertEquals("Dr. Smith", result.headName());
        assertEquals(150L, result.studentCount());
        assertEquals(new BigDecimal(75000.0), result.avgSalary());
    }

    @Test
    void testGetDepartmentStats_DepartmentNotFound() {
        when(departmentRepository.findByNameFuzzy("NonExistent"))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> universityService.getDepartmentStats("NonExistent"));
    }

    @Test
    void testGetDepartmentStats_WithNullHead() {
        Department dept = new Department();
        dept.setName("Engineering");
        dept.setHead(null);

        when(departmentRepository.findByNameFuzzy("Engineering"))
                .thenReturn(Optional.of(dept));
        when(studentRepository.countByDepartmentName("Engineering"))
                .thenReturn(200L);
        when(lecturerRepository.getAverageSalaryByDepartment("Engineering"))
                .thenReturn(Optional.of(new BigDecimal(80000.0)));

        DepartmentStatsDTO result = universityService.getDepartmentStats("Engineering");

        assertEquals("None", result.headName());
    }

    @Test
    void testSearchPeople_Success() {
        Student student = new Student();
        student.setId(1L);
        student.setFullName("John Doe");

        PersonSearchProjection projection = mockPersonSearch();

        when(studentRepository.searchAll("John")).thenReturn(List.of(projection));

        List<PersonSearchDTO> result = universityService.searchPeople("John");

        assertEquals(1, result.size());
        verify(studentRepository).searchAll("John");
    }

    @Test
    void testSearchPeople_EmptyResult() {
        when(studentRepository.searchAll("XYZ")).thenReturn(List.of());

        List<PersonSearchDTO> result = universityService.searchPeople("XYZ");

        assertTrue(result.isEmpty());
    }

    @Test
    void testGetTopStudents_Success() {
        Student student1 = new Student();
        student1.setId(1L);
        Student student2 = new Student();
        student2.setId(2L);

        StudentProjection studentProjection1 = mockStudent();
        StudentProjection studentProjection2 = mockStudent();

        when(studentRepository.findTopByCourse("Math", 2))
                .thenReturn(List.of(studentProjection1, studentProjection2));

        List<StudentDTO> result = universityService.getTopStudents("Math", 2);

        assertEquals(2, result.size());
        verify(studentRepository).findTopByCourse("Math", 2);
    }

    @Test
    void testGetCoursesByProfessor_Success() {
        Course course = new Course();
        course.setId(1L);
        course.setTitle("Java Programming");

        CourseProjection courseProjection = mockCourse();

        when(courseRepository.findAllByProfessorName("Dr. Johnson"))
                .thenReturn(List.of(courseProjection));

        List<CourseDTO> result = universityService.getCoursesByProfessor("Dr. Johnson");

        assertEquals(1, result.size());
        verify(courseRepository).findAllByProfessorName("Dr. Johnson");
    }

    @Test
    void testGetStudentGrades_Success() {
        when(studentRepository.existsById(1L)).thenReturn(true);

        CourseGradeProjection projection = mockCourseGrade();

        when(studentRepository.findGradesByStudentId(1L))
                .thenReturn(List.of(projection));

        List<CourseGradeDTO> result = universityService.getStudentGrades(1L);

        assertEquals(1, result.size());
        verify(studentRepository).existsById(1L);
    }

    @Test
    void testGetStudentGrades_StudentNotFound() {
        when(studentRepository.existsById(999L)).thenReturn(false);

        assertThrows(RuntimeException.class,
                () -> universityService.getStudentGrades(999L));
    }

    @Test
    void testGetAverageSalariesByDepartments_Success() {
        // In your Test:
        AvgDepartmentSalaryDTO avgSalary1 = new AvgDepartmentSalaryDTO("Computer Science", 5000.0);
        AvgDepartmentSalaryDTO avgSalary2 = new AvgDepartmentSalaryDTO("Mathematics", 4500.0);

        List<AvgDepartmentSalaryDTO> salaries = List.of(
                avgSalary1,
                avgSalary2);

        when(lecturerRepository.getAverageSalaryByDepartments())
                .thenReturn(salaries);

        List<AvgDepartmentSalaryDTO> result = universityService.getAverageSalariesByDepartments();

        assertEquals(2, result.size());
        verify(lecturerRepository, times(2)).getAverageSalaryByDepartments();
    }

    private PersonSearchProjection mockPersonSearch() {
        return new PersonSearchProjection() {
            @Override
            public String getName() {
                return null;
            }

            @Override
            public String getRole() {
                return null;
            }

            @Override
            public String getDepartmentName() {
                return null;
            }
        };
    }

    private StudentProjection mockStudent() {
        return new StudentProjection() {
            @Override
            public String getFullName() {
                return null;
            }

            @Override
            public String getDepartmentName() {
                return null;
            }

            @Override
            public BigDecimal getGpa() {
                return null;
            }
        };
    }

    private CourseProjection mockCourse() {
        return new CourseProjection() {
            @Override
            public String getTitle() {
                return null;
            }

            @Override
            public Integer getCredits() {
                return null;
            }

            @Override
            public String getStudentNames() {
                return null;
            }
        };
    }

    private CourseGradeProjection mockCourseGrade() {
        return new CourseGradeProjection() {
            @Override
            public String getCourseTitle() {
                return null;
            }

            @Override
            public BigDecimal getGrade() {
                return null;
            }
        };
    }
}