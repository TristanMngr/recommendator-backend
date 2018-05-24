package com.isep.recommendator.app.custom_object;

import com.isep.recommendator.app.model.Concept;
import com.isep.recommendator.app.model.Module;
import com.isep.recommendator.app.model.Speciality;

public class Spe_Module_Concept {

    private Speciality speciality;
    private Module module;
    private Concept concept;

    public Spe_Module_Concept(Speciality speciality, Module module, Concept concept){
        this.speciality = speciality;
        this.module = module;
        this.concept = concept;
    }

    public Speciality getSpeciality() {
        return speciality;
    }

    public void setSpeciality(Speciality speciality) {
        this.speciality = speciality;
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    public Concept getConcept() {
        return concept;
    }

    public void setConcept(Concept concept) {
        this.concept = concept;
    }
}
