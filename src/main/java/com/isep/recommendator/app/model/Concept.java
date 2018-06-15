package com.isep.recommendator.app.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "concept")
public class Concept {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long concept_id;

    @NotBlank
    @Column(unique = true)
    private String name;

    @ManyToMany(fetch = FetchType.EAGER,cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "concept_module",
            joinColumns = {@JoinColumn(name = "concept_id")},
            inverseJoinColumns = {@JoinColumn(name = "module_id")}
    )
    @JsonBackReference
    private Set<Module> modules = new HashSet<>();

    @OneToMany(mappedBy = "concept", cascade = { CascadeType.MERGE, CascadeType.PERSIST })
    @JsonBackReference
    private Set<Requirement> requirements = new HashSet<>();

    public Concept() {
    }

    public Concept(String name) {
        this.setName(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return concept_id;
    }

    public void setId(Long concept_id) {
        this.concept_id = concept_id;
    }

    public Set<Module> getModules() {
        return modules;
    }
}
