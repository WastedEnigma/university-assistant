package com.ai.universityassistant.dto;

import com.ai.universityassistant.repository.projection.PersonSearchProjection;

public record PersonSearchDTO(String name, String role, String department) {

    public static PersonSearchDTO of(PersonSearchProjection personSearchProjection) {
        return new PersonSearchDTO(personSearchProjection.getName(), personSearchProjection.getRole(), personSearchProjection.getDepartmentName());
    }
}
