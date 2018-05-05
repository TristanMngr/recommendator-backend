package com.isep.recommendator.app.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name="speciality_module")
public class SpecialityModule {
    @ManyToOne
    @JoinColumn(name = "speciality_id")
    private Speciality speciality;

    @ManyToOne
    @JoinColumn(name = "module_id")
    private Module module;

    @Column(name = "main_module")
    private boolean mainModule;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long speciality_module_id;
}


