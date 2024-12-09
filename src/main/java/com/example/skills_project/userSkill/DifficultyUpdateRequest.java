package com.example.skills_project.userSkill;

import com.example.skills_project.validation.Difficulty;
import com.example.skills_project.validation.ValueOfEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DifficultyUpdateRequest {
    @ValueOfEnum(enumClass = Difficulty.class, message = "O valor deve ser um dos seguintes: DOMINADO, FACIL, MEDIO, DIFICIL.")
    private String difficultyRating;
}
