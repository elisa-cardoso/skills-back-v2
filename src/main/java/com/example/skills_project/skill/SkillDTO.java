package com.example.skills_project.skill;

import com.example.skills_project.category.CategoryDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class SkillDTO {
    private Long id;
    private String title;
    private String image;
    private String description;
    private Set<CategoryDTO> category;
}
