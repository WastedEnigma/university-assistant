package com.ai.universityassistant.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "departments")
@Getter
@Setter
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    // The specific lecturer who runs the department
    @OneToOne
    @JoinColumn(name = "head_id")
    private Lecturer head;

    // One department has many students
    @OneToMany(mappedBy = "department")
    private Set<Student> students = new HashSet<>();

    // Bidirectional Many-to-Many with Lecturers
    @ManyToMany(mappedBy = "departments")
    private Set<Lecturer> lecturers = new HashSet<>();
}
