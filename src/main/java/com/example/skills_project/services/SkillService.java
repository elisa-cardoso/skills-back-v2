package com.example.skills_project.services;

import com.example.skills_project.skill.Skill;
import com.example.skills_project.skill.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class SkillService {
    @Autowired
    private SkillRepository skillRepository;

    public Page<Skill> getSkills(String title, Long categoryId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        if (categoryId != null && title != null) {
            return skillRepository.findByCategoryAndTitle(categoryId, title, pageable);
        }
        else if (categoryId != null) {
            return skillRepository.findByCategory_Id(categoryId, pageable);
        }
        else if (title != null) {
            return skillRepository.findByTitle(title, pageable);
        }
        else {
            return skillRepository.findAllSkills(pageable);
        }
    }
}
