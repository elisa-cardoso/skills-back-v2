package com.example.skills_project.userSkill;


import lombok.*;

@Setter
@Getter
@Data

public class UserSkillUpdateDTO {
    private Long id;
    private String skillName;
    private Integer level;
    private String username;
    private String skillImageUrl;

    public UserSkillUpdateDTO(Long id, String skillName, Integer level, String username, String skillImageUrl) {
        this.id = id;
        this.skillName = skillName;
        this.level = level;
        this.username = username;
        this.skillImageUrl = skillImageUrl;
    }

}
