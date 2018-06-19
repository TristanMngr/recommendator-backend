package com.isep.recommendator.app.service;

import com.isep.recommendator.app.handler.BadRequestException;
import com.isep.recommendator.app.handler.CustomValidationException;
import com.isep.recommendator.app.handler.ResourceNotFoundException;
import com.isep.recommendator.app.model.Concept;
import com.isep.recommendator.app.model.Module;
import com.isep.recommendator.app.model.Requirement;
import com.isep.recommendator.app.repository.ConceptRepository;
import com.isep.recommendator.app.repository.RequirementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Service
public class ConceptService {

    private final ConceptRepository     conceptRepo;
    private final RequirementRepository requirementRepository;

    @Autowired
    public ConceptService(ConceptRepository conceptRepo, RequirementRepository requirementRepository){
        this.conceptRepo = conceptRepo;
        this.requirementRepository = requirementRepository;
    }

    public Concept get(Long id){
        Optional concept = conceptRepo.findById(id);
        if (concept.isPresent())
            return (Concept) concept.get();
        else
            throw new ResourceNotFoundException("no concept found with id " + id);
    }

    public List<Concept> getAll(){
        return conceptRepo.findAll();
    }

    public Concept create(String name) throws BadRequestException{
        if (conceptRepo.findByName(name) != null)
            throw new BadRequestException("concept with name " + name + " already exist");

        try {
            @Valid Concept concept = new Concept(name);
            return conceptRepo.save(concept);
        } catch (ConstraintViolationException e){
            throw new CustomValidationException(e);
        }

    }


    public Concept delete(Concept concept){
        for (Module module: concept.getModules()) {
            module.getConcepts().remove(concept);
        }

        requirementRepository.deleteRequirementByConceptId(concept.getId());

        conceptRepo.delete(concept);
        return concept;
    }

    public Concept update(Concept concept, String new_name) throws BadRequestException{
        if (conceptRepo.findByName(new_name) != null)
            throw new BadRequestException("concept with name " + new_name + " already exist");

        concept.setName(new_name);
        return conceptRepo.save(concept);
    }

}
