package com.example.skills_project.userSkill;

import com.example.skills_project.validation.Difficulty;
import com.example.skills_project.validation.ValueOfEnum;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSkillRequestDTO {

    @NotNull(message = "O ID da habilidade é obrigatório.")
    private Long skillId;

    @NotNull
    @Min(value = 1, message = "O nível mínimo permitido é 1.")
    @Max(value = 5, message = "O nível máximo permitido é 5.")
    private Integer level;

    @ValueOfEnum(enumClass = Difficulty.class, message = "O valor deve ser um dos seguintes: DOMINADO, FACIL, MEDIO, DIFICIL.")
    private String difficultyRating;
}
