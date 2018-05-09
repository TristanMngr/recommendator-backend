package com.isep.recommendator.app.model;

import java.util.List;

public class SpecialityConceptQueryResponse {

    private Speciality speciality;
    private Concept concept;

    public SpecialityConceptQueryResponse(Speciality speciality, Concept concept){
        this.speciality = speciality;
        this.concept = concept;
    }

    public Concept getConcept() {
        return concept;
    }

    public void setConcept(Concept concept) {
        this.concept = concept;
    }

    public Speciality getSpeciality() {
        return speciality;
    }

    public void setSpeciality(Speciality speciality) {
        this.speciality = speciality;
    }
}
