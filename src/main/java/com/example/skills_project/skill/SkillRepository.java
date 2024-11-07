package com.example.skills_project.skill;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SkillRepository extends JpaRepository<Skill, Long> {
    Optional<Skill> findById(Long id);
    @Query("SELECT s FROM skills s WHERE LOWER(REPLACE(s.title, ' ', '')) LIKE LOWER(REPLACE(CONCAT('%', :title, '%'), ' ', ''))")
    List<Skill> searchByTitle(@Param("title") String title);

    List<Skill> findByCategory_Id(Long categoryId);
}
