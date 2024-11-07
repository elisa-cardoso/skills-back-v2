package com.example.skills_project.skill;

import com.example.skills_project.category.CategoryDTO;

import java.util.Set;
import java.util.stream.Collectors;

public record SkillResponseDTO(Long id, String title, String image, String description, Set<CategoryDTO> category) {
    public SkillResponseDTO(Skill skill){
        this(skill.getId(), skill.getTitle(), skill.getImage(), skill.getDescription(), skill.getCategory().stream()
                .map(c -> new CategoryDTO(c.getId(), c.getName()))
                .collect(Collectors.toSet()));
    }
}
