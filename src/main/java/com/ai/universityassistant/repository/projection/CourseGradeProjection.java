package com.ai.universityassistant.repository.projection;

import java.math.BigDecimal;

public interface CourseGradeProjection {
    String getCourseTitle();
    BigDecimal getGrade();
}
