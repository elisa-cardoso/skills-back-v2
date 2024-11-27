package com.example.skills_project.services;

import com.example.skills_project.exception.QuestionNotFoundException;
import com.example.skills_project.exception.ResourceNotFoundException;
import com.example.skills_project.exception.SkillNotFoundException;
import com.example.skills_project.exception.UserSkillNotFoundException;
import com.example.skills_project.question.Question;
import com.example.skills_project.question.QuestionRepository;
import com.example.skills_project.skill.Skill;
import com.example.skills_project.skill.SkillRepository;
import com.example.skills_project.userSkill.UserSkill;
import com.example.skills_project.userSkill.UserSkillRepository;
import com.example.skills_project.users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class SkillAssessmentService {
    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserSkillRepository userSkillRepository;

    @Autowired
    private SkillRepository skillRepository;

    public Question getRandomQuestionForSkill(Long skillId) {
        List<Question> questions = questionRepository.findBySkillId(skillId);
        if (questions.isEmpty()) {
            throw new SkillNotFoundException("Nenhuma questão disponível para essa skill.");
        }
        return questions.get(new Random().nextInt(questions.size()));
    }

    public Question createQuestion(Question question) {
        return questionRepository.save(question);
    }

    public void deleteQuestion(Long questionId) {
        questionRepository.deleteById(questionId);
    }

    public boolean checkAnswerAndAssignLevel(Long questionId, String answer) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        Optional<Question> questionOpt = questionRepository.findById(questionId);
        if (!questionOpt.isPresent()) {
            throw new QuestionNotFoundException("Questão não encontrada com o id " + questionId);
        }

        Question question = questionOpt.get();
        Skill skill = question.getSkill();

        UserSkill userSkill = userSkillRepository.findByUserAndSkill(user, skill)
                .orElse(new UserSkill(user, skill, 0, 0));

        boolean isCorrect = answer.equalsIgnoreCase(question.getCorrectOption());

        if (isCorrect) {
            userSkill.setScore(userSkill.getScore() + 1);

            int newLevel = calculateLevel(userSkill.getScore());

            if (userSkill.getLevel() != newLevel) {
                userSkill.setLevel(newLevel);
            }

            userSkillRepository.save(userSkill);
        }

        return isCorrect;
    }

    public Integer calculateLevel(Integer score) {
        if (score <= 1) {
            return 1; // Nível 1
        } else if (score <= 2) {
            return 2; // Nível 2
        } else if (score <= 3) {
            return 3; // Nível 3
        } else if (score <= 4) {
            return 4; // Nível 4
        }else {
            return 5; // Nível 5
        }
    }

    public UserSkill getUserSkill(User user, Long skillId) {
        Skill skill = skillRepository.findById(skillId)
                .orElseThrow(() -> new SkillNotFoundException("Skill não encontrada com o id " + skillId));

        Optional<UserSkill> userSkillOpt = userSkillRepository.findByUserAndSkill(user, skill);

        return userSkillOpt.orElseThrow(() -> new UserSkillNotFoundException("Associação não encontrada."));
    }

    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    public Question getQuestionById(Long questionId) {
        return questionRepository.findById(questionId)
                .orElseThrow(() -> new QuestionNotFoundException("Questão não encontrada com o id: " + questionId));
    }

    public Question updateQuestion(Question question) {
        return questionRepository.save(question);
    }

    public List<Question> getQuestionsBySkillId(Long skillId) {
        return questionRepository.findBySkillId(skillId);
    }


}


