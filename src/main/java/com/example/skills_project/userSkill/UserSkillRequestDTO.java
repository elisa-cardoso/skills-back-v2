package com.example.skills_project.userSkill;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSkillRequestDTO {
    private Long skillId;
    @NotNull
    @Min(1)
    private Integer level;
}
