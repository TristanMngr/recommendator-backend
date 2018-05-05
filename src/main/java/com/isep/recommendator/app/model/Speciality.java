package com.isep.recommendator.app.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name="speciality")
public class Speciality {
    @ManyToMany(mappedBy = "jobs")

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long speciality_id;

    @NotBlank
    private String name;

    private String description;
}
