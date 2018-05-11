package com.isep.recommendator.app.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

@Entity
@Table(name = "speciality_module",
        uniqueConstraints = @UniqueConstraint(columnNames = {"speciality_id", "module_id" })
)
public class SpecialityModule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long speciality_module_id;

    @ManyToOne
    @JoinColumn(name = "speciality_id")
    @JsonBackReference
    private Speciality speciality;

    @ManyToOne
    @JoinColumn(name = "module_id")
    @JsonBackReference
    private Module module;

    @Column(name = "main_module")
    private boolean mainModule;

    public SpecialityModule() {
    }

    public SpecialityModule(Speciality speciality, Module module, boolean mainModule) {
        this.speciality = speciality;
        this.module = module;
        this.mainModule = mainModule;
    }

    public Speciality getSpeciality() {
        return speciality;
    }

    public void setSpeciality(Speciality speciality) {
        this.speciality = speciality;
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    public boolean isMainModule() {
        return mainModule;
    }

    public void setMainModule(boolean mainModule) {
        this.mainModule = mainModule;
    }

    public Long getId() {
        return speciality_module_id;
    }

    public void setId(Long speciality_module_id) {
        this.speciality_module_id = speciality_module_id;
    }
}
