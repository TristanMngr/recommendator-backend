package com.isep.recommendator.app.custom_object;
import com.isep.recommendator.app.model.Speciality;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Form2Response implements Form {

    private Speciality speciality;
    private int matching;
    private List<ModuleWithMatchingConcepts> matching_modules;

    public Form2Response(Speciality speciality, Map<Long, ModuleWithMatchingConcepts> matching_modules, int matching) {
        this.speciality = speciality;
        this.matching_modules = new ArrayList<>(matching_modules.values());
        this.matching = matching;
    }

    public Form2Response(Speciality speciality) {
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
    }
}

