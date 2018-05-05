package com.isep.recommendator.app.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="job")
public class Job {
    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "job_module",
            joinColumns = { @JoinColumn(name = "job_id") },
            inverseJoinColumns = { @JoinColumn(name = "speciality_id") }
    )
    private Set<Speciality> specialities = new HashSet<>();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long job_id;

    @NotBlank
    private String name;

    private String description;




}
