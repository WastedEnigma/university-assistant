package com.ai.universityassistant.dto;

import java.math.BigDecimal;

import com.ai.universityassistant.repository.projection.StudentProjection;

public record StudentDTO(String name, String departmentName, BigDecimal gpa) {

    public static StudentDTO of(StudentProjection studentProjection) {
        return new StudentDTO(studentProjection.getFullName(), studentProjection.getDepartmentName(), studentProjection.getGpa());
    }
}
