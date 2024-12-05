package com.example.skills_project.skill;

import com.example.skills_project.category.CategoryDTO;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class SkillDTO {
    private Long id;

    @Size(min = 1, max = 100, message = "O título deve ter entre 1 e 100 caracteres.")
    private String title;

    @Size(min = 5, message = "A URL da imagem deve ter no mínimo 5 caracteres.")
    private String image;

    @Size(min = 10, message = "A descrição deve ter no mínimo 10 caracteres.")
    private String description;
    private Set<CategoryDTO> category;
}
