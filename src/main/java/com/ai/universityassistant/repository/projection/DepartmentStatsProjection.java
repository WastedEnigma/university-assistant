package com.ai.universityassistant.repository.projection;

import java.math.BigDecimal;

public interface DepartmentStatsProjection {
    String getName();
    String getHeadName();
    Long getStudentCount();
    BigDecimal getAvgSalary();
}
