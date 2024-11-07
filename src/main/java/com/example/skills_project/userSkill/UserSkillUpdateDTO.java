package com.example.skills_project.userSkill;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

public class UserSkillUpdateDTO {
    private Long id;
    private String skillName;
    private Integer level;
    private String username;
    private String skillImageUrl;


    public UserSkillUpdateDTO() {}

    public UserSkillUpdateDTO(Long id, String skillName, Integer level, String username, String skillImageUrl) {
        this.id = id;
        this.skillName = skillName;
        this.level = level;
        this.username = username;
        this.skillImageUrl = skillImageUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSkillImageUrl() {
        return skillImageUrl;
    }

    public void setSkillImageUrl(String skillImageUrl) {
        this.skillImageUrl = skillImageUrl;
    }
}
