package com.example.skills_project.controller;

import com.example.skills_project.category.Category;
import com.example.skills_project.category.CategoryRepository;
import com.example.skills_project.services.SkillService;
import com.example.skills_project.skill.Skill;
import com.example.skills_project.skill.SkillRepository;
import com.example.skills_project.skill.SkillRequestDTO;
import com.example.skills_project.skill.SkillResponseDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("skill")
public class SkillController {

    @Autowired
    private SkillRepository repository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SkillService skillService;

    @PostMapping
    public ResponseEntity<SkillResponseDTO> saveSkill(@RequestBody @Valid SkillRequestDTO data){
        Skill skillData = new Skill(data);

        if (data.category() != null) {
            Set<Category> category = data.category().stream()
                    .map(id -> categoryRepository.findById(id)
                            .orElseThrow(() -> new RuntimeException("Categoria não encontrada com id: " + id)))
                    .collect(Collectors.toSet());
            skillData.setCategory(category);
        }

        this.repository.save(skillData);
        return ResponseEntity.status(HttpStatus.CREATED).body(new SkillResponseDTO(skillData));
    }

    @GetMapping
    public ResponseEntity<List<SkillResponseDTO>> getAll(){
        List<SkillResponseDTO> skillList = this.repository.findAll().stream()
                .map(SkillResponseDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(skillList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SkillResponseDTO> getSkillById(@PathVariable Long id) {
        Skill skillData = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Habilidade não encontrada com o id: " + id));

        return ResponseEntity.ok(new SkillResponseDTO(skillData));
    }

    @PutMapping("/{id}")
    public SkillResponseDTO updateSkill(@PathVariable Long id, @RequestBody SkillRequestDTO data) {
        Skill skillData = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Skill not found with id: " + id));

        skillData.setTitle(data.title());
        skillData.setImage(data.image());
        skillData.setDescription(data.description());

        if (data.category() != null && !data.category().isEmpty()) {
            Set<Category> categories = data.category().stream()
                    .map(categoryId -> categoryRepository.findById(categoryId)
                            .orElseThrow(() -> new RuntimeException("Category not found with id: " + categoryId)))
                    .collect(Collectors.toSet());
            skillData.setCategory(categories);
        }

        return new SkillResponseDTO(repository.save(skillData));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSkill(@PathVariable Long id) {
        try {
            Skill skillData = repository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Skill not found with id " + id));

            repository.delete(skillData);
            return ResponseEntity.ok().build(); // Retorna sucesso
        } catch (Exception e) {
            System.err.println("Erro ao deletar a skill: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<Page<Skill>> searchSkills(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "categoryId", required = false) Long categoryId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        // System.out.println("title: " + title + ", categoryId: " + categoryId + ", page: " + page + ", size: " + size);

        Page<Skill> skills = skillService.getSkills(title, categoryId, page, size);
        return ResponseEntity.ok(skills);
    }

    @GetMapping("/by-category/{categoryId}")
    public Page<Skill> getSkillsByCategory(@PathVariable Long categoryId, Pageable pageable) {
        return repository.findByCategory_Id(categoryId, pageable);
    }

}
