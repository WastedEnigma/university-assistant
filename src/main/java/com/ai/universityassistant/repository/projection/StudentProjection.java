package com.ai.universityassistant.repository.projection;

import java.math.BigDecimal;

public interface StudentProjection {
    String getFullName();
    String getDepartmentName();
    BigDecimal getGpa();
}
