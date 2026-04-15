package com.ai.universityassistant.dto;

import java.math.BigDecimal;

import lombok.Builder;

@Builder
public record DepartmentStatsDTO(
    String name,
    String headName,
    long studentCount,
    BigDecimal avgSalary) {
}
