package com.isep.recommendator.app.model;

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

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "concept_module",
            joinColumns = {@JoinColumn(name = "concept_id")},
            inverseJoinColumns = {@JoinColumn(name = "module_id")}
    )
    private Set<Speciality> modules = new HashSet<>();

    public Concept() {
    }

    public Concept(String name){
        this.setName(name);
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return concept_id;
    }

    public Set<Speciality> getModules() {
        return modules;
    }

    public void setName(String name) {
        this.name = name;
    }


    public void setModules(Set<Speciality> modules) {
        this.modules = modules;
    }

    public void setId(Long concept_id){
        this.concept_id = concept_id;
    }
}
