package com.example.skills_project.userSkill;

import com.example.skills_project.skill.Skill;
import com.example.skills_project.users.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
public class UserSkill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @ManyToOne
    @JoinColumn(name = "skill_id")
    private Skill skill;

    @Column(nullable = false)
    private Integer level;

    private Integer score;

    @Column(name = "favorite")
    private Boolean favorite = false;

    public UserSkill(User user, Skill skill, Integer level, Integer score) {
        this.user = user;
        this.skill = skill;
        this.level = (level == null) ? 1 : level;
        this.score = (score == null) ? 0 : score;

    }
    public Integer getScore() {
        return score != null ? score : 0;
    }
}
