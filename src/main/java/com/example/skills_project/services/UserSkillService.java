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
import java.util.Optional;
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String login = authentication.getName();
        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Skill skill = skillRepository.findById(skillId)
                .orElseThrow(() -> new ResourceNotFoundException("Skill not found"));

        UserSkill userSkill = new UserSkill();
        userSkill.setUser(user);
        userSkill.setSkill(skill);
        userSkill.setLevel(level);

        UserSkill savedUserSkill = userSkillRepository.save(userSkill);

        return new UserSkillResponseDTO(
                savedUserSkill.getId(),
                skill.getId(),
                user.getLogin(),
                skill.getTitle(),
                savedUserSkill.getLevel(),
                savedUserSkill.getScore(),
                skill.getImage(),
                skill.getDescription(),
                userSkill.getFavorite()
        );
    }

    public List<UserSkillResponseDTO> getUserSkillsByUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String login = authentication.getName();
        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        List<UserSkill> userSkills = userSkillRepository.findByUser(user);

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
                    skill.getDescription(),
                    userSkill.getFavorite()
            );
        }).collect(Collectors.toList());
    }

    public UserSkill getUserSkillById(Long id) {
        return userSkillRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("UserSkill not found"));
    }



    public UserSkillUpdateDTO updateUserSkill(Long id, Integer level) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String login = authentication.getName();
        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        UserSkill userSkill = userSkillRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("UserSkill not found"));

        if (!userSkill.getUser().getUsername().equals(user.getUsername())) {
            throw new ResourceNotFoundException("UserSkill does not belong to the authenticated user");
        }

        userSkill.setLevel(level);
        userSkill = userSkillRepository.save(userSkill);

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

    public List<UserSkill> getFavorites() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String login = authentication.getName();

        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        return userSkillRepository.findByUserAndFavoriteTrue(user);
    }

    public String addFavorite(Long skillId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String login = authentication.getName(); // Obtém o login do usuário autenticado

        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        Skill skill = skillRepository.findById(skillId)
                .orElseThrow(() -> new ResourceNotFoundException("Habilidade não encontrada"));

        Optional<UserSkill> userSkillOptional = userSkillRepository.findByUserAndSkill(user, skill);
        if (userSkillOptional.isPresent()) {
            UserSkill userSkill = userSkillOptional.get();

            if (userSkill.getFavorite() != null && userSkill.getFavorite()) {
                return "A habilidade já está nos favoritos";
            }

            userSkill.setFavorite(true);
            userSkillRepository.save(userSkill);
            return "Habilidade favoritada com sucesso!";
        } else {
            return "Habilidade não encontrada para o usuário.";
        }
    }

    public String removeFavorite(Long skillId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String login = authentication.getName(); // Obtém o login do usuário autenticado

        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        Skill skill = skillRepository.findById(skillId)
                .orElseThrow(() -> new ResourceNotFoundException("Habilidade não encontrada"));

        Optional<UserSkill> userSkillOptional = userSkillRepository.findByUserAndSkill(user, skill);
        if (userSkillOptional.isPresent()) {
            UserSkill userSkill = userSkillOptional.get();

            if (userSkill.getFavorite() == null || !userSkill.getFavorite()) {
                return "A habilidade não está nos favoritos";
            }

            userSkill.setFavorite(false);
            userSkillRepository.save(userSkill);
            return "Habilidade removida dos favoritos com sucesso!";
        }

        return "Habilidade não encontrada para o usuário.";
    }


}
