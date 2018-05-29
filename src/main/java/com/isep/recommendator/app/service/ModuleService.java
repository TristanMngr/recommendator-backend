package com.isep.recommendator.app.service;

import com.isep.recommendator.app.handler.CustomValidationException;
import com.isep.recommendator.app.handler.ResourceNotFoundException;
import com.isep.recommendator.app.model.Concept;
import com.isep.recommendator.app.model.Module;
import com.isep.recommendator.app.repository.ConceptRepository;
import com.isep.recommendator.app.repository.ModuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.util.List;
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
            throw new ResourceNotFoundException("no module found with id " + id);
    }

    public List<Module> getAll(){
        return moduleRepo.findAll();
    }

    public Module addConcept(Module module, Concept concept){
        concept.getModules().add(module);
        module.getConcepts().add(concept);
        return moduleRepo.save(module);
    }

    public Module create(String name, String description){
        try {
            @Valid Module user = new Module(name, description);
            moduleRepo.save(user);
            return user;
        } catch (ConstraintViolationException e){
            throw new CustomValidationException(e);
        }
    }

    public Module delete(Module module){
        moduleRepo.delete(module);
        return module;
    }

}
