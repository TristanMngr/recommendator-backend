package com.isep.recommendator.app.custom_object;

import com.isep.recommendator.app.model.Concept;
import com.isep.recommendator.app.model.Module;

import java.util.ArrayList;
import java.util.List;

public class ModuleWithMatchingConcepts {

    private Module module;
    private List<Concept> matching_concepts;

    public ModuleWithMatchingConcepts(Module module, Concept c){
        this.module = module;
        this.matching_concepts = new ArrayList<>();
        this.matching_concepts.add(c);
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    public void setMatching_concepts(List<Concept> matching_concepts) {
        this.matching_concepts = matching_concepts;
    }

    public List<Concept> getMatching_concepts() {
        return matching_concepts;
    }

    public void addMatching_concept(Concept concept){
        this.matching_concepts.add(concept);
    }
}
