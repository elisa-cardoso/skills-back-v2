package com.example.skills_project.skill;

import java.util.Set;

public record SkillRequestDTO(Long id, String title, String image, String description, Set<Long> category) {

}
