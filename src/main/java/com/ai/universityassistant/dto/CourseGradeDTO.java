package com.ai.universityassistant.dto;

import java.math.BigDecimal;

import com.ai.universityassistant.repository.projection.CourseGradeProjection;

public record CourseGradeDTO(String title, BigDecimal grade) {

    public static CourseGradeDTO of(CourseGradeProjection courseGradeProjection) {
        return new CourseGradeDTO(courseGradeProjection.getCourseTitle(), courseGradeProjection.getGrade());
    }
}
