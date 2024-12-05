package com.example.skills_project.userSkill;

import com.example.skills_project.skill.Skill;
import com.example.skills_project.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserSkillRepository extends JpaRepository<UserSkill, Long> {
    Optional<UserSkill> findByUserAndSkill (User user, Skill skill);
    List<UserSkill> findByUser(User user);
    List<UserSkill> findByUserAndFavoriteTrue(User user);
    @Query("SELECT us FROM UserSkill us WHERE us.user = :user ORDER BY us.favorite DESC")
    List<UserSkill> findUserSkillsByUserOrderedByFavorite(@Param("user") User user);
}
