package com.example.skills_project.services;

import com.example.skills_project.exception.ResourceNotFoundException;
import com.example.skills_project.exception.SkillNotFoundException;
import com.example.skills_project.exception.UserSkillNotFoundException;
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

    public List<UserSkill> getUserSkillsByUserOrderedByFavorite(User user) {
        return userSkillRepository.findUserSkillsByUserOrderedByFavorite(user);
    }

    public UserSkillResponseDTO associateSkillToUser(Long skillId, Integer level, String difficultyRating) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String login = authentication.getName();
        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado."));

        Skill skill = skillRepository.findById(skillId)
                .orElseThrow(() -> new SkillNotFoundException("Habilidade não encontrada."));

        UserSkill userSkill = new UserSkill();
        userSkill.setUser(user);
        userSkill.setSkill(skill);
        userSkill.setLevel(level);

        if (difficultyRating != null) {
            userSkill.setDifficultyRating(difficultyRating);
        }


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
                userSkill.getFavorite(),
                userSkill.getDifficultyRating()
        );
    }


    public List<UserSkillResponseDTO> getUserSkillsByUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String login = authentication.getName();
        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado."));

        List<UserSkill> userSkills = userSkillRepository.findUserSkillsByUserOrderedByFavorite(user);

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
                    userSkill.getFavorite(),
                    userSkill.getDifficultyRating()
            );
        }).collect(Collectors.toList());
    }

    public UserSkill getUserSkillById(Long id) {
        return userSkillRepository.findById(id)
                .orElseThrow(() -> new UserSkillNotFoundException("Associação não encontrada."));
    }

    public UserSkillUpdateDTO updateUserSkill(Long id, Integer level) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String login = authentication.getName();
        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado."));

        UserSkill userSkill = userSkillRepository.findById(id)
                .orElseThrow(() -> new UserSkillNotFoundException("Associação não encontrada."));

        if (!userSkill.getUser().getUsername().equals(user.getUsername())) {
            throw new ResourceNotFoundException("Essa associação não pertence ao usuário autenticado.");
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
                .orElseThrow(() -> new UserSkillNotFoundException("Associação não encontrada."));
        userSkillRepository.delete(userSkill);
    }

    public List<UserSkill> getFavorites() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String login = authentication.getName();

        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado."));

        return userSkillRepository.findByUserAndFavoriteTrue(user);
    }

    public String addFavorite(Long skillId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String login = authentication.getName();

        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado."));

        Skill skill = skillRepository.findById(skillId)
                .orElseThrow(() -> new SkillNotFoundException("Habilidade não encontrada."));

        Optional<UserSkill> userSkillOptional = userSkillRepository.findByUserAndSkill(user, skill);
        if (userSkillOptional.isPresent()) {
            UserSkill userSkill = userSkillOptional.get();

            if (userSkill.getFavorite() != null && userSkill.getFavorite()) {
                return "A habilidade já está nos favoritos.";
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
        String login = authentication.getName();

        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado."));

        Skill skill = skillRepository.findById(skillId)
                .orElseThrow(() -> new SkillNotFoundException("Habilidade não encontrada."));

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

    public String updateDifficultyRating(Long skillId, String difficultyRating) {
        if (!isValidDifficulty(difficultyRating)) {
            return "A dificuldade deve ser 'dominado', 'fácil', 'médio' ou 'difícil'.";
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String login = authentication.getName();
        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado."));

        Skill skill = skillRepository.findById(skillId)
                .orElseThrow(() -> new SkillNotFoundException("Habilidade não encontrada."));

        Optional<UserSkill> userSkillOptional = userSkillRepository.findByUserAndSkill(user, skill);
        if (userSkillOptional.isPresent()) {
            UserSkill userSkill = userSkillOptional.get();

            userSkill.setDifficultyRating(difficultyRating);
            userSkillRepository.save(userSkill);
            return "Dificuldade atribuída com sucesso!";
        } else {
            return "Associação entre usuário e habilidade não encontrada.";
        }
    }

    private boolean isValidDifficulty(String difficultyRating) {
        return difficultyRating.equals("dominado") || difficultyRating.equals("fácil") ||
                difficultyRating.equals("médio") || difficultyRating.equals("difícil");
    }

}
