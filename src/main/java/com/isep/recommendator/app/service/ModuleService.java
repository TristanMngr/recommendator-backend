package com.isep.recommendator.app.service;

import com.isep.recommendator.app.model.Concept;
import com.isep.recommendator.app.model.Module;
import com.isep.recommendator.app.repository.ConceptRepository;
import com.isep.recommendator.app.repository.ModuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ModuleService {

    private final ModuleRepository moduleRepo;
    private final ConceptRepository conceptRepo;

    @Autowired
    public ModuleService(ModuleRepository moduleRepo, ConceptRepository conceptRepo){
        this.moduleRepo = moduleRepo;
        this.conceptRepo = conceptRepo;
    }

    public Module get(Long id){
        Optional module = moduleRepo.findById(id);
        if (module.isPresent())
            return (Module) module.get();
        else
            return null;
    }

    public Module addConcept(Module module, Concept concept){
        module.getConcepts().add(concept);
        concept.getModules().add(module);
        return moduleRepo.saveAndFlush(module);
    }

}
