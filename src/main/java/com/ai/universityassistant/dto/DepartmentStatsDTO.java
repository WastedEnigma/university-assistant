package com.ai.universityassistant.dto;

import java.math.BigDecimal;

import com.ai.universityassistant.repository.projection.DepartmentStatsProjection;

import lombok.Builder;

@Builder
public record DepartmentStatsDTO(
    String name,
    String headName,
    long studentCount,
    BigDecimal avgSalary) {

    public static DepartmentStatsDTO of(DepartmentStatsProjection projection) {
        return DepartmentStatsDTO.builder()
                .name(projection.getName())
                .headName(projection.getHeadName())
                .studentCount(projection.getStudentCount())
                .avgSalary(projection.getAvgSalary())
                .build();
    }
}
