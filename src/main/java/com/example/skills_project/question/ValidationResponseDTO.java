package com.example.skills_project.question;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValidationResponseDTO {
    private boolean correct;
    private Integer score;
    private Integer level;

    public ValidationResponseDTO(boolean correct, Integer score, Integer level) {
        this.correct = correct;
        this.score = score;
        this.level = level;
    }

}
