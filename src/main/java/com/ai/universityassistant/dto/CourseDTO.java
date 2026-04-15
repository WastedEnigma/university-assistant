package com.ai.universityassistant.dto;

import com.ai.universityassistant.repository.projection.CourseProjection;

public record CourseDTO(String title, Integer credits, String students) {

    public static CourseDTO of(CourseProjection courseProjection) {
        return new CourseDTO(courseProjection.getTitle(),
                courseProjection.getCredits(),
                courseProjection.getStudentNames());
    }
}
