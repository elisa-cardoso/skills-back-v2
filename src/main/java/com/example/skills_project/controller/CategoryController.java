package com.example.skills_project.controller;

import com.example.skills_project.category.Category;
import com.example.skills_project.category.CategoryDTO;
import com.example.skills_project.category.CategoryRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("categories")
public class CategoryController {
    private final CategoryRepository categoryRepository;

    public CategoryController(CategoryRepository categoriaRepository) {
        this.categoryRepository = categoriaRepository;
    }

    @GetMapping
    public List<CategoryDTO> getAllCategory() {
        return categoryRepository.findAll().stream()
                .map(category -> new CategoryDTO(category.getId(), category.getName()))
                .collect(Collectors.toList());
    }

    @PostMapping
    public CategoryDTO createCategory(@RequestBody CategoryDTO categoryDTO) {
        Category category = new Category();
        category.setName(categoryDTO.getName());
        Category savedCategory = categoryRepository.save(category);
        return new CategoryDTO(savedCategory.getId(), savedCategory.getName());
    }

    @PutMapping("/{id}")
    public CategoryDTO updateCategory (@PathVariable Long id, @RequestBody CategoryDTO categoryDTO) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada com o ID: " + id));

        category.setName(categoryDTO.getName());
        Category updatedCategory = categoryRepository.save(category);
        return new CategoryDTO(updatedCategory.getId(), updatedCategory.getName());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada com o ID: " + id));

        categoryRepository.delete(category);
        return ResponseEntity.noContent().build();
    }
}
