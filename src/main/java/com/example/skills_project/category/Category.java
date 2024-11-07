package com.example.skills_project.category;

import com.example.skills_project.skill.Skill;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "category")
@Data
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToMany(mappedBy = "category")
    @JsonIgnoreProperties("category")
    private Set<Skill> skills = new HashSet<>();
}