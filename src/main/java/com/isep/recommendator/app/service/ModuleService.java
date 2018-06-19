package com.isep.recommendator.app.service;

import com.isep.recommendator.app.handler.BadRequestException;
import com.isep.recommendator.app.handler.CustomValidationException;
import com.isep.recommendator.app.handler.ResourceNotFoundException;
import com.isep.recommendator.app.model.Concept;
import com.isep.recommendator.app.model.Module;
import com.isep.recommendator.app.repository.ConceptRepository;
import com.isep.recommendator.app.repository.ModuleRepository;
import com.isep.recommendator.app.repository.SpecialityModuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Service
public class ModuleService {

    private final ModuleRepository           moduleRepo;
    private final ConceptRepository          conceptRepo;
    private final SpecialityModuleRepository specialityModuleRepository;

    @Autowired
    public ModuleService(ModuleRepository moduleRepo, ConceptRepository conceptRepo, SpecialityModuleRepository specialityModuleRepository){
        this.moduleRepo = moduleRepo;
        this.conceptRepo = conceptRepo;
        this.specialityModuleRepository = specialityModuleRepository;
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

    public Module removeConcept(Module module, Concept concept) throws BadRequestException {
        if (!module.getConcepts().contains(concept))
            throw new BadRequestException("concept with id "+ concept.getId() + " isn't in module with id " + module.getId());

        concept.getModules().remove(module);
        module.getConcepts().remove(concept);
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
        // du coup j'ai fait une query j'avais trop de galère à le faire de manière classique
        // mais je pense que c'est pas plus mal, on boucle pas comme ça
        specialityModuleRepository.deleteSpecialityModuleByModuleId(module.getId());

        for (Concept concept : module.getConcepts()) {
            concept.getModules().remove(module);
        }

        moduleRepo.delete(module);

        return module;
    }

    public Module update(Module module, String new_name, String new_desc) throws BadRequestException{
        if (!module.getName().equals(new_name)) {
            if (!moduleRepo.findByName(new_name).isEmpty())
                throw new BadRequestException("module with name " + new_name + " already exist");
            module.setName(new_name);
        }
        if (!module.getDescription().equals(new_desc))
            module.setDescription(new_desc);

        return moduleRepo.save(module);
    }

}
