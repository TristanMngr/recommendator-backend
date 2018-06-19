package com.isep.recommendator.app.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "requirement")
public class Requirement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long requirement_id;

    @ManyToOne
    @JoinColumn(name = "concept_id")
    private Concept concept;

    @NotBlank
    private String note_type;

    @NotNull
    private Integer note;

    private String mooc;

    private String question;

    public Requirement(@NotBlank Concept concept, @NotBlank String note_type, @NotNull Integer note, String mooc, String question) {
        this.concept = concept;
        this.note_type = note_type;
        this.note = note;
        this.mooc = mooc;
        this.question = question;
    }

    public Requirement() {
    }

    public String getMooc() {
        return mooc;
    }

    public void setMooc(String mooc) {
        this.mooc = mooc;
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

    public Integer getNote() {
        return note;
    }

    public void setNote(Integer note) {
        this.note = note;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}
