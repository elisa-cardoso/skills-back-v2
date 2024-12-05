package com.example.skills_project.userSkill;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserSkillRequest {
    @NotNull
    @Min(value = 1, message = "O nível mínimo permitido é 1.")
    @Max(value = 5, message = "O nível máximo permitido é 5.")
    private Integer level;
}
