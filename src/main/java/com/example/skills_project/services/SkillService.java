package com.example.skills_project.services;

import com.example.skills_project.exception.ResourceNotFoundException;
import com.example.skills_project.skill.Skill;
import com.example.skills_project.skill.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class SkillService {
    @Autowired
    private SkillRepository skillRepository;

    public Page<Skill> getSkills(String title, Long categoryId, int page, int size, String sortField, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortField);
        Pageable pageable = PageRequest.of(page -1, size, sort);

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
