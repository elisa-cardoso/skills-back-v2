package com.example.skills_project.question;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findBySkillId(Long skillId);
    Optional<Question> findById(Long id);
}
