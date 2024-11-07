package com.example.skills_project.controller;

import com.example.skills_project.category.Category;
import com.example.skills_project.category.CategoryRepository;
import com.example.skills_project.skill.Skill;
import com.example.skills_project.skill.SkillRepository;
import com.example.skills_project.skill.SkillRequestDTO;
import com.example.skills_project.skill.SkillResponseDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("skill")
public class SkillController {

    // Injeção de dependência do SkillRepository, que será utilizado para interagir com o banco de dados.
    @Autowired
    private SkillRepository repository;

    @Autowired
    private CategoryRepository categoryRepository;

    @PostMapping
    public ResponseEntity<SkillResponseDTO> saveSkill(@RequestBody @Valid SkillRequestDTO data){
        // Converte os dados recebidos no DTO para uma entidade Skill.
        Skill skillData = new Skill(data);

        // Associa as categorias à skill
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
        // Recupera todas as skills do banco de dados, mapeia para um SkillResponseDTO e retorna como lista.
        List<SkillResponseDTO> skillList = this.repository.findAll().stream()
                .map(SkillResponseDTO::new) // Converte cada Skill em um SkillResponseDTO.
                .collect(Collectors.toList()); // Converte o Stream para uma lista.
        return ResponseEntity.ok(skillList); // Retorna a lista de DTOs.
    }

    @GetMapping("/{id}")
    public ResponseEntity<SkillResponseDTO> getSkillById(@PathVariable Long id) {
        Skill skillData = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Habilidade não encontrada com o id: " + id));

        return ResponseEntity.ok(new SkillResponseDTO(skillData));
    }

    @PutMapping("/{id}")
    public SkillResponseDTO updateSkill(@PathVariable Long id, @RequestBody SkillRequestDTO data) {
        // Encontrar a skill pelo ID
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

    // A chave estrangeira não esta configurada como cascata. Isso foi feito para não ocorrer a exclusão automática das referências na tabela user_skill, pois isso pode levar à exclusão em massa de dados relacionados.

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSkill(@PathVariable Long id) {
        try {
            Skill skillData = repository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Skill not found with id " + id));

            repository.delete(skillData);
            return ResponseEntity.ok().build(); // Retorna sucesso
        } catch (Exception e) {
            System.err.println("Erro ao deletar a skill: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Retorna erro
        }
    }

    // filtro de skill por título
    @GetMapping("/search")
    public List<Skill> searchSkills(@RequestParam String title) {
        return repository.searchByTitle(title);
    }

    // filtro para buscar skills por categoria
    @GetMapping("/by-category/{categoryId}")
    public List<Skill> getSkillsByCategory(@PathVariable Long categoryId) {
        return repository.findByCategory_Id(categoryId);
    }

}
