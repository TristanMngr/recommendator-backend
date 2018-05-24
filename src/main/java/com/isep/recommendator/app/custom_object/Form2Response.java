package com.isep.recommendator.app.custom_object;
import com.isep.recommendator.app.model.Speciality;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Form2Response {

    private Speciality speciality;
    private List<ModuleWithMatchingConcepts> matching_modules;
    private int matching;

    public Form2Response(Speciality speciality, Map<Long, ModuleWithMatchingConcepts> matching_modules){
        this.speciality = speciality;
        this.matching_modules = new ArrayList<>(matching_modules.values());
        this.calculateMatching();
    }

    public Form2Response(Speciality speciality){
        this.speciality = speciality;
        this.matching_modules = new ArrayList<>();
        this.matching = 0;
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

    public List<ModuleWithMatchingConcepts> getMatching_modules() {
        return matching_modules;
    }

    public void setModules(List<ModuleWithMatchingConcepts> matching_modules) {
        this.matching_modules = matching_modules;
        this.calculateMatching();
    }

    private void calculateMatching(){
        int score = 0;
        for (ModuleWithMatchingConcepts m : matching_modules) {
            score += m.getMatching_concepts().size();
        }
        this.matching = score;
    }
}

