package com.example.skills_project.skill;

import com.example.skills_project.category.Category;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Table(name = "skills")
@Entity(name = "skills")

// em runtime declara os métodos
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")

public class Skill {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String image;
    private String description;

    @ManyToMany
    @JoinTable(
            name = "skill_categoria",
            joinColumns = @JoinColumn(name = "skill_id"),
            inverseJoinColumns = @JoinColumn(name = "categoria_id")
    )
    @JsonIgnoreProperties("skills")
    private Set<Category> category = new HashSet<>();

    public Skill(SkillRequestDTO data){
        this.title = data.title();
        this.image = data.image();
        this.description = data.description();

    }
}
