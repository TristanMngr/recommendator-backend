package com.isep.recommendator.app.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "job")
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long job_id;

    @NotBlank
    @Column(unique = true)
    private String name;

    private String description;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonBackReference
    @JoinTable(
            name = "job_speciality",
            joinColumns = {@JoinColumn(name = "job_id")},
            inverseJoinColumns = {@JoinColumn(name = "speciality_id")},
            uniqueConstraints = @UniqueConstraint(columnNames = {"job_id", "speciality_id" })
    )
    private Set<Speciality> specialities = new HashSet<>();

    public Job() {
    }

    public Job(String name, String description) {
        this.name = name;
        this.description = description;
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

    public Set<Speciality> getSpecialities() {
        return specialities;
    }

    public Long getId() {
        return job_id;
    }

    public void setId(Long job_id) {
        this.job_id = job_id;
    }
}
