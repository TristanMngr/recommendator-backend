package com.isep.recommendator.app.custom_object;

import com.isep.recommendator.app.model.Concept;
import com.isep.recommendator.app.model.Speciality;

import java.util.ArrayList;
import java.util.List;

public class SpecialityAndMatchingConceptsObject {

    private Speciality speciality;
    private List<Concept> matching_concepts;
    private int matching;

    public SpecialityAndMatchingConceptsObject(Speciality speciality, List<Concept> matching_concepts){
        this.speciality = speciality;
        this.matching_concepts = matching_concepts;
        // TODO vraiment le calculer
        this.matching = 50;
    }

    public SpecialityAndMatchingConceptsObject(Speciality speciality){
        this.speciality = speciality;
        this.matching_concepts = new ArrayList<>();
        this.matching = 0;
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

    public int getMatching() {
        return matching;
    }

    public void setMatching(int matching) {
        this.matching = matching;
    }
}
