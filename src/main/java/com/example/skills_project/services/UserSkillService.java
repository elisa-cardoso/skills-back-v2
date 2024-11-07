package com.example.skills_project.services;

import com.example.skills_project.exception.ResourceNotFoundException;
import com.example.skills_project.skill.Skill;
import com.example.skills_project.skill.SkillRepository;
import com.example.skills_project.userSkill.*;
import com.example.skills_project.users.User;
import com.example.skills_project.users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserSkillService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private UserSkillRepository userSkillRepository;

    public UserSkillResponseDTO associateSkillToUser(Long skillId, Integer level) {
        // Obtém o usuário autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String login = authentication.getName();
        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Obtém a skill com base no ID fornecido
        Skill skill = skillRepository.findById(skillId)
                .orElseThrow(() -> new ResourceNotFoundException("Skill not found"));

        // Cria e salva a associação entre o usuário e a skill
        UserSkill userSkill = new UserSkill();
        userSkill.setUser(user);
        userSkill.setSkill(skill);
        userSkill.setLevel(level);

        UserSkill savedUserSkill = userSkillRepository.save(userSkill);

        // Mapeia para UserSkillResponseDTO
        return new UserSkillResponseDTO(
                savedUserSkill.getId(),
                skill.getId(),
                user.getLogin(),
                skill.getTitle(),
                savedUserSkill.getLevel(),
                savedUserSkill.getScore(),
                skill.getImage(),
                skill.getDescription()
        );
    }

    public List<UserSkillResponseDTO> getUserSkillsByUser() {
        // Obtém o usuário autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String login = authentication.getName();
        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Retorna a lista de skills associadas ao usuário
        List<UserSkill> userSkills = userSkillRepository.findByUser(user);

        // Mapeia UserSkill para UserSkillResponseDTO
        return userSkills.stream().map(userSkill -> {
            Skill skill = userSkill.getSkill();
            return new UserSkillResponseDTO(
                    userSkill.getId(),
                    skill.getId(),
                    user.getLogin(),
                    skill.getTitle(),
                    userSkill.getLevel(),
                    userSkill.getScore(),
                    skill.getImage(),
                    skill.getDescription()
            );
        }).collect(Collectors.toList());
    }

    public UserSkill getUserSkillById(Long id) {
        return userSkillRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("UserSkill not found"));
    }



    public UserSkillUpdateDTO updateUserSkill(Long id, Integer level) {
        // Obtém o usuário autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String login = authentication.getName();
        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Encontra a UserSkill pelo id
        UserSkill userSkill = userSkillRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("UserSkill not found"));

        // Verifica se a UserSkill pertence ao usuário autenticado
        if (!userSkill.getUser().getUsername().equals(user.getUsername())) {
            throw new ResourceNotFoundException("UserSkill does not belong to the authenticated user");
        }

        // Atualiza o nível da UserSkill
        userSkill.setLevel(level);
        userSkill = userSkillRepository.save(userSkill);

        // Retorna o DTO atualizado
        return new UserSkillUpdateDTO(
                userSkill.getId(),
                userSkill.getSkill().getTitle(),
                userSkill.getLevel(),
                userSkill.getUser().getLogin(),
                userSkill.getSkill().getImage()
        );
    }



    public void deleteUserSkill(Long id) {
        UserSkill userSkill = userSkillRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("UserSkill not found"));
        userSkillRepository.delete(userSkill);
    }
}
