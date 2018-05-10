package com.isep.recommendator.app.custom_object;

import com.isep.recommendator.app.model.Concept;
import com.isep.recommendator.app.model.Speciality;

import java.util.List;

public class SpecialityAndConceptObject {

    private Speciality speciality;
    private Concept concept;

    public SpecialityAndConceptObject(Speciality speciality, Concept concept){
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
