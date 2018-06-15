package com.isep.recommendator.app.service;

import com.isep.recommendator.app.handler.CustomValidationException;
import com.isep.recommendator.app.handler.ResourceNotFoundException;
import com.isep.recommendator.app.model.Concept;
import com.isep.recommendator.app.model.Module;
import com.isep.recommendator.app.model.Requirement;
import com.isep.recommendator.app.repository.RequirementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Service
public class RequirementService {
    private final RequirementRepository requirementRepository;
    private final ConceptService conceptService;

    @Autowired
    public RequirementService(RequirementRepository requirementRepository, ConceptService conceptService) {
        this.requirementRepository = requirementRepository;
        this.conceptService = conceptService;
    }

    public List<Requirement> getAll() {
        return requirementRepository.findAll();
    }

    public Requirement get(Long id){
        Optional requirement = requirementRepository.findById(id);
        if (requirement.isPresent())
            return (Requirement) requirement.get();
        else
            throw new ResourceNotFoundException("no requirement found with id " + id);
    }

    public Requirement create(Long concept_id, String note_type, int note){
        try {
            Concept concept = conceptService.get(concept_id);
            @Valid Requirement requirement = new Requirement(concept, note_type, note);
            requirementRepository.save(requirement);
            return requirement;
        } catch (ConstraintViolationException e){
            throw new CustomValidationException(e);
        }
    }
}
