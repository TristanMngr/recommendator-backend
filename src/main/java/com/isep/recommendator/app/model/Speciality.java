package com.isep.recommendator.app.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="speciality")
public class Speciality {
    @ManyToMany(mappedBy = "specialities")
    private Set<Job> jobs = new HashSet<>();

    @OneToMany(mappedBy = "speciality")
    private Set<SpecialityModule> specialityModules = new HashSet<SpecialityModule>();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long speciality_id;

    @NotBlank
    private String name;

    private String description;
}
