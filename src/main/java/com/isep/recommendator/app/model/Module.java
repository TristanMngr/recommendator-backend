package com.isep.recommendator.app.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "module")
public class Module implements Serializable {
    @OneToMany(mappedBy = "module")
    private Set<SpecialityModule> specialityModules = new HashSet<SpecialityModule>();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long module_id;

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    public Module(){

    }

    public Module(String name, String description){
        this.setName(name);
        this.setDescription(description);
    }

    public String getDescription() {
        return description;
    }

    public Long getId() {
        return module_id;
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
}

