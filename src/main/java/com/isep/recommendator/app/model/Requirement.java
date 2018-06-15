package com.isep.recommendator.app.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "requirement")
public class Requirement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long requirement_id;

    @ManyToOne
    @JoinColumn(name = "concept_id")
    @JsonBackReference
    private Concept concept;

    @NotBlank
    @Column(unique = true)
    private String note_type;

    @NotBlank
    private int note;

    private String question;

    public Requirement(Concept concept, @NotBlank String note_type, @NotBlank int note) {
        this.concept = concept;
        this.note_type = note_type;
        this.note = note;
    }

    public Long getRequirement_id() {
        return requirement_id;
    }

    public Concept getConcept() {
        return concept;
    }

    public void setConcept(Concept concept) {
        this.concept = concept;
    }

    public void setRequirement_id(Long requirement_id) {
        this.requirement_id = requirement_id;
    }

    public String getNote_type() {
        return note_type;
    }

    public void setNote_type(String note_type) {
        this.note_type = note_type;
    }

    public int getNote() {
        return note;
    }

    public void setNote(int note) {
        this.note = note;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}
