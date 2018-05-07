package com.isep.recommendator.app.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "module")
public class Module implements Serializable {
    @OneToMany(mappedBy = "module", cascade = CascadeType.ALL)
    private Set<SpecialityModule> specialityModules = new HashSet<SpecialityModule>();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long module_id;

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @ManyToMany(mappedBy = "modules", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonManagedReference
    private Set<Concept> concepts = new HashSet<>();

    public Module(){

    }

    public Module(String name, String description){
        this.setName(name);
        this.setDescription(description);
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<SpecialityModule> getSpecialityModules() {
        return specialityModules;
    }

    public void setSpecialityModules(Set<SpecialityModule> specialityModules) {
        this.specialityModules = specialityModules;
    }

    public Long getId() {
        return module_id;
    }

    public void setId(Long module_id) {
        this.module_id = module_id;
    }

    public Set<Concept> getConcepts() {
        return concepts;
    }

    public void setConcepts(Set<Concept> concepts) {
        this.concepts = concepts;
    }
}

