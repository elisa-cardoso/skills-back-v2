package com.example.skills_project.skill;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record SkillRequestDTO(
        Long id,

        @Size(min = 1, max = 100, message = "O título deve ter entre 1 e 100 caracteres.")
        String title,

        @Size(min = 5, message = "A URL da imagem deve ter no mínimo 5 caracteres.")
        String image,

        @Size(min = 10, message = "A descrição deve ter no mínimo 10 caracteres.")
        String description,

        Set<Long> category) {

}
