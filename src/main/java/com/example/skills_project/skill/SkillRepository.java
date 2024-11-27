package com.example.skills_project.skill;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SkillRepository extends JpaRepository<Skill, Long> {
    Optional<Skill> findById(Long id);
    @Query("SELECT s FROM skills s JOIN s.category c WHERE (c.id = :categoryId OR :categoryId IS NULL) " +
            "AND (LOWER(TRIM(s.title)) LIKE LOWER(CONCAT('%', TRIM(:title), '%')) OR :title IS NULL)")
    Page<Skill> findByCategoryAndTitle(
            @Param("categoryId") Long categoryId,
            @Param("title") String title,
            Pageable pageable
    );

    @Query("SELECT s FROM skills s WHERE LOWER(TRIM(s.title)) LIKE LOWER(CONCAT('%', TRIM(:title), '%'))")
    Page<Skill> findByTitle(
            @Param("title") String title,
            Pageable pageable
    );

    @Query("SELECT s FROM skills s")
    Page<Skill> findAllSkills(Pageable pageable);

    @Query("SELECT s FROM skills s JOIN s.category c WHERE c.id = :categoryId")
    Page<Skill> findByCategory_Id(
            @Param("categoryId") Long categoryId,
            Pageable pageable
    );
}
