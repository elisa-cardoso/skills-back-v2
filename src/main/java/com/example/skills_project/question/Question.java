package com.example.skills_project.question;


import com.example.skills_project.skill.Skill;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "questions")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinColumn(name = "skill_id", nullable = false)
    @NotNull(message = "A habilidade da questão é necessária.")
    private Skill skill;

    @Column(name = "question_text", nullable = false)
    @NotNull(message = "O texto da questão é necessário.")
    private String questionText;

    @Column(name = "option_a", nullable = false)
    @NotNull(message = "O texto da opção A é necessário.")
    private String optionA;

    @Column(name = "option_b", nullable = false)
    @NotNull(message = "O texto da opção B é necessário.")
    private String optionB;

    @Column(name = "option_c", nullable = false)
    @NotNull(message = "O texto da opção C é necessário.")
    private String optionC;

    @Column(name = "option_d", nullable = false)
    @NotNull(message = "O texto da opção D é necessário.")
    private String optionD;

    @Column(name = "correct_option", nullable = false)
    @NotNull(message = "Indique a opção correta.")
    private String correctOption;

    @Column(name = "is_answer_correct")
    private Boolean isAnswerCorrect;
}


