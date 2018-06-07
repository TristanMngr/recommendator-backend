package com.isep.recommendator.app.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "speciality")
public class Speciality {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long speciality_id;

    @Column(unique = true)
    @NotBlank
    private String name;

    private String description;

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "specialities", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<Job> jobs = new HashSet<>();
    
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "speciality", cascade = CascadeType.ALL)
    private Set<SpecialityModule> specialityModules = new HashSet<SpecialityModule>();

    public Speciality() {
    }

    public Speciality(@NotBlank String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Set<Job> getJobs() {
        return jobs;
    }

    public void setJobs(Set<Job> jobs) {
        this.jobs = jobs;
    }

    public Set<SpecialityModule> getSpecialityModules() {
        return specialityModules;
    }

    public void setSpecialityModules(Set<SpecialityModule> specialityModules) {
        this.specialityModules = specialityModules;
    }

    public Long getId() {
        return speciality_id;
    }

    public void setId(Long speciality_id) {
        this.speciality_id = speciality_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
