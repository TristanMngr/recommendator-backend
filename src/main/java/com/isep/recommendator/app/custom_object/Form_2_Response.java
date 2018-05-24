package com.isep.recommendator.app.custom_object;
import com.isep.recommendator.app.model.Speciality;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Form_2_Response {

    private Speciality speciality;
    private List<ModuleWithMatchingConcepts> modules;
    private int matching;

    public Form_2_Response(Speciality speciality, Map<Long, ModuleWithMatchingConcepts> modules){
        this.speciality = speciality;
        this.modules = new ArrayList<>(modules.values());
        this.calculateMatching();
    }

    public Form_2_Response(Speciality speciality){
        this.speciality = speciality;
        this.modules = new ArrayList<>();
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

    public List<ModuleWithMatchingConcepts> getModules() {
        return modules;
    }

    public void setModules(List<ModuleWithMatchingConcepts> modules) {
        this.modules = modules;
        this.calculateMatching();
    }

    //TODO prendre en compte main module
    private void calculateMatching(){
        int score = 0;
        for (ModuleWithMatchingConcepts m : modules) {
            score += m.getMatching_concepts().size();
        }
        this.matching = score;
    }
}

