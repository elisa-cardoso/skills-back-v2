package com.example.skills_project.userSkill;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserSkillResponseDTO {
    private Long id;
    private Long skillId;
    private String userLogin;
    private String skillName;
    private Integer level;
    private Integer score;
    private String image;
    private String description;
    private Boolean favorite;
    private String difficultyRating;

    public UserSkillResponseDTO(Long id, Long skillId, String userLogin, String skillName, Integer level, Integer score, String image, String description, Boolean favorite, String difficultyRating) {
        this.id = id;
        this.skillId = skillId;
        this.userLogin = userLogin;
        this.skillName = skillName;
        this.level = level;
        this.score = score;
        this.image = image;
        this.description = description;
        this.favorite = favorite;
        this.difficultyRating = difficultyRating;
    }
}

