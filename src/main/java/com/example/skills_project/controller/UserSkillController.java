package com.example.skills_project.controller;

import com.example.skills_project.services.SkillAssessmentService;
import com.example.skills_project.services.UserSkillService;
import com.example.skills_project.skill.SkillRepository;
import com.example.skills_project.userSkill.*;
import com.example.skills_project.users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/user_skills")
public class UserSkillController {

    @Autowired
    private UserSkillService userSkillService;

    @Autowired
    private UserSkillRepository userSkillRepository;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserSkillResponseDTO> associateSkillToUser(@RequestBody UserSkillRequestDTO requestDTO) {
        UserSkillResponseDTO userSkillResponseDTO = userSkillService.associateSkillToUser(requestDTO.getSkillId(), requestDTO.getLevel());
        return ResponseEntity.status(HttpStatus.CREATED).body(userSkillResponseDTO);
    }

    @GetMapping
    public ResponseEntity<List<UserSkillResponseDTO>> getUserSkills() {
        List<UserSkillResponseDTO> userSkills = userSkillService.getUserSkillsByUser();
        return ResponseEntity.ok(userSkills);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserSkill> getUserSkillById(@PathVariable Long id) {
        UserSkill userSkill = userSkillService.getUserSkillById(id);
        return ResponseEntity.ok(userSkill);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserSkillUpdateDTO> updateUserSkill(
            @PathVariable Long id,
            @RequestBody UpdateUserSkillRequest request) {
        UserSkillUpdateDTO updatedUserSkill = userSkillService.updateUserSkill(id, request.getLevel());
        return ResponseEntity.ok(updatedUserSkill);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserSkill(@PathVariable Long id) {
        userSkillService.deleteUserSkill(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/favorite/{skillId}")
    public ResponseEntity<String> addFavorite(@PathVariable Long skillId) {
        String response = userSkillService.addFavorite(skillId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/favorite/{skillId}")
    public ResponseEntity<String> removeFavorite(@PathVariable Long skillId) {
        String response = userSkillService.removeFavorite(skillId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("toggle/{id}")
    public ResponseEntity<UserSkill> toggleFavorite(@PathVariable("id") Long userSkillId) {
        Optional<UserSkill> userSkillOpt = userSkillRepository.findById(userSkillId);
        if (userSkillOpt.isPresent()) {
            UserSkill userSkill = userSkillOpt.get();
            userSkill.setFavorite(!userSkill.getFavorite());
            userSkillRepository.save(userSkill);
            return ResponseEntity.ok(userSkill);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/favorite")
    public ResponseEntity<?> getFavorites() {
        return ResponseEntity.ok(userSkillService.getFavorites());
    }
}
