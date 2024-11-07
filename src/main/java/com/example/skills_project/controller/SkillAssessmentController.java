package com.example.skills_project.controller;

import com.example.skills_project.question.*;
import com.example.skills_project.services.SkillAssessmentService;
import com.example.skills_project.skill.Skill;
import com.example.skills_project.userSkill.UserSkill;
import com.example.skills_project.userSkill.UserSkillRepository;
import com.example.skills_project.userSkill.UserSkillResponseDTO;
import com.example.skills_project.users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/questions")
public class SkillAssessmentController {
    @Autowired
    private SkillAssessmentService skillAssessmentService;

    @Autowired
    private UserSkillRepository userSkillRepository;

    @Autowired
    private QuestionRepository questionRepository;


    @GetMapping
    public ResponseEntity<List<Question>> getAllQuestions() {
        List<Question> questions = skillAssessmentService.getAllQuestions();
        return ResponseEntity.ok(questions);
    }

    @GetMapping("/{skillId}")
    public ResponseEntity<Question> getRandomQuestion(@PathVariable Long skillId) {
        Question question = skillAssessmentService.getRandomQuestionForSkill(skillId);
        return ResponseEntity.ok(question);
    }

    @GetMapping("/skill/{skillId}")
    public ResponseEntity<List<QuestionResponseDTO>> getQuestionsBySkillId(@PathVariable Long skillId) {
        List<Question> questions = skillAssessmentService.getQuestionsBySkillId(skillId);

        if (questions.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        // Mapear a lista de entidades para DTOs de resposta
        List<QuestionResponseDTO> response = questions.stream()
                .map(QuestionResponseDTO::new) // Converte a entidade para DTO de resposta
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{skillId}")
    public ResponseEntity<Question> createQuestion(
            @PathVariable Long skillId,
            @RequestBody QuestionDTO questionDTO) {

        Skill skill = new Skill();
        skill.setId(skillId); // Atribua o ID da habilidade

        // Converte o DTO em uma entidade Question
        Question question = new Question();
        question.setSkill(skill); // Define a habilidade
        question.setQuestionText(questionDTO.getQuestionText());
        question.setOptionA(questionDTO.getOptionA());
        question.setOptionB(questionDTO.getOptionB());
        question.setOptionC(questionDTO.getOptionC());
        question.setOptionD(questionDTO.getOptionD());
        question.setCorrectOption(questionDTO.getCorrectOption());

        Question savedQuestion = skillAssessmentService.createQuestion(question);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedQuestion);
    }

    @PutMapping("/{questionId}")
    public ResponseEntity<Question> updateQuestion(
            @PathVariable Long questionId,
            @RequestBody QuestionDTO questionDTO) {

        // Recupera a questão existente
        Question existingQuestion = skillAssessmentService.getQuestionById(questionId);

        existingQuestion.setQuestionText(questionDTO.getQuestionText());
        existingQuestion.setOptionA(questionDTO.getOptionA());
        existingQuestion.setOptionB(questionDTO.getOptionB());
        existingQuestion.setOptionC(questionDTO.getOptionC());
        existingQuestion.setOptionD(questionDTO.getOptionD());
        existingQuestion.setCorrectOption(questionDTO.getCorrectOption());

        Question updatedQuestion = skillAssessmentService.updateQuestion(existingQuestion);
        return ResponseEntity.ok(updatedQuestion);
    }

    @DeleteMapping("/{questionId}")
    public ResponseEntity<String> deleteQuestion(@PathVariable Long questionId) {
        skillAssessmentService.deleteQuestion(questionId);
        return ResponseEntity.ok("Questão excluída com sucesso.");
    }

    @PostMapping("/{questionId}/validate")
    public ResponseEntity<ValidationResponseDTO> validateAnswer(
            @PathVariable Long questionId,
            @RequestBody AnswerRequest answerRequest) {

        String answer = answerRequest.getAnswer();
        System.out.println("Resposta recebida do frontend: " + answer);

        boolean isCorrect = skillAssessmentService.checkAnswerAndAssignLevel(questionId, answer);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        Optional<Question> questionOpt = questionRepository.findById(questionId);
        if (!questionOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ValidationResponseDTO(false, 0, 1));
        }

        Question question = questionOpt.get();
        System.out.println("Resposta correta da questão: " + question.getCorrectOption());
        System.out.println("Comparando resposta: " + answer + " com " + question.getCorrectOption());

        Skill skill = question.getSkill();

        UserSkill userSkill = userSkillRepository.findByUserAndSkill(user, skill)
                .orElseThrow(() -> new RuntimeException("UserSkill não encontrado"));

        ValidationResponseDTO response = new ValidationResponseDTO(isCorrect, userSkill.getScore(), userSkill.getLevel());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/user-skill/{skillId}")
    public ResponseEntity<UserSkillResponseDTO> getUserSkill(@PathVariable Long skillId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        UserSkill userSkill = skillAssessmentService.getUserSkill(user, skillId);

        if (userSkill == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }

        UserSkillResponseDTO response = new UserSkillResponseDTO(
                userSkill.getId(),
                userSkill.getSkill().getId(),
                userSkill.getUser().getLogin(),
                userSkill.getSkill().getTitle(),
                userSkill.getLevel(),
                userSkill.getScore(),
                userSkill.getSkill().getImage(),
                userSkill.getSkill().getDescription()

        );

        return ResponseEntity.ok(response);
    }


}