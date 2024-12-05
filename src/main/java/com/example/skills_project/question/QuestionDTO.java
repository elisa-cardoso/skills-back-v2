package com.example.skills_project.question;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionDTO {
    private Long id;

    @NotNull(message = "O texto da questão é obrigatório.")
    @Size(min = 5, max = 500, message = "O texto da questão deve ter entre 10 e 500 caracteres.")
    private String questionText;

    @NotNull(message = "A opção A é obrigatória.")
    @Size(min = 1, max = 100, message = "A opção A deve ter entre 1 e 100 caracteres.")
    private String optionA;

    @NotNull(message = "A opção B é obrigatória.")
    @Size(min = 1, max = 100, message = "A opção B deve ter entre 1 e 100 caracteres.")
    private String optionB;

    @NotNull(message = "A opção C é obrigatória.")
    @Size(min = 1, max = 100, message = "A opção C deve ter entre 1 e 100 caracteres.")
    private String optionC;

    @NotNull(message = "A opção D é obrigatória.")
    @Size(min = 1, max = 100, message = "A opção D deve ter entre 1 e 100 caracteres.")
    private String optionD;

    @Pattern(regexp = "^[A-D]$", message = "A opção correta deve ser uma letra entre A e D.")
    private String correctOption;
}
