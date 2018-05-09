package com.isep.recommendator.app.model;

import java.util.List;

public class SpecialityAndMatchingConceptsList {

    private Speciality speciality;
    private List<Concept> matching_concepts;

    public SpecialityAndMatchingConceptsList(Speciality speciality, List<Concept> matching_concepts){
        this.speciality = speciality;
        this.matching_concepts = matching_concepts;
    }

    public List<Concept> getMatching_concepts() {
        return matching_concepts;
    }

    public void setMatching_concepts(List<Concept> matching_concepts) {
        this.matching_concepts = matching_concepts;
    }

    public Speciality getSpeciality() {
        return speciality;
    }

    public void setSpeciality(Speciality speciality) {
        this.speciality = speciality;
    }
}
