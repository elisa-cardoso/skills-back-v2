package com.example.skills_project.userSkill;

import com.example.skills_project.skill.Skill;
import com.example.skills_project.users.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserSkillRepository extends JpaRepository<UserSkill, Long> {
    Optional<UserSkill> findByUserAndSkill (User user, Skill skill);
    List<UserSkill> findByUser(User user);
}
