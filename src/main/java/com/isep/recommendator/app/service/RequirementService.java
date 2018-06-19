package com.isep.recommendator.app.service;

import com.isep.recommendator.app.handler.BadRequestException;
import com.isep.recommendator.app.handler.CustomValidationException;
import com.isep.recommendator.app.handler.ResourceNotFoundException;
import com.isep.recommendator.app.model.Concept;
import com.isep.recommendator.app.model.Requirement;
import com.isep.recommendator.app.repository.RequirementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

    public Requirement create(Long concept_id, String note_type, Integer note, String mooc, String question) throws BadRequestException {
        try {
            checkNotation(note_type, note);
        } catch (BadRequestException e) {
            throw new BadRequestException(e.getMessage());
        }

        try {
            Concept concept = conceptService.get(concept_id);
            @Valid Requirement requirement = new Requirement(concept, note_type, note, mooc, question);
            requirementRepository.save(requirement);
            return requirement;
        } catch (ConstraintViolationException e){
            throw new CustomValidationException(e);
        }
    }

    public Requirement destroy(Long requirementId) {
        Requirement requirement = requirementFound(requirementId);

        requirementRepository.delete(requirement);

        return requirement;
    }

    public Requirement requirementFound(Long requirementId) throws ResourceNotFoundException {
        Optional<Requirement> requirement = requirementRepository.findById(requirementId);

        if (!requirement.isPresent())
            throw new ResourceNotFoundException("Requirement with id " + requirementId + " not found");

        return requirement.get();
    }


    public void checkNotation(String note_type, int note) throws BadRequestException {
        List<String> notations_types = new ArrayList<>();
        for ( String key : notations().keySet() ) {
            notations_types.add(key);
        }

        if (!notations_types.contains(note_type)) {
            throw new BadRequestException("Note type " + note_type + " does not exist");
        }
        if (!notations().get(note_type).contains(note)) {
            throw new BadRequestException("Note is not include in " + note_type + " type of note");
        }
    }


    /**
     * Map of authorize notation
     * @return
     */
    public HashMap<String, Set<Integer>> notations() {
        Set<Integer> number_notation_range  = IntStream.rangeClosed(0, 20).boxed().collect(Collectors.toSet());
        Set<Integer> comment_notation_range = IntStream.rangeClosed(0, 4).boxed().collect(Collectors.toSet());
        Set<Integer> binary_notaion_range  = IntStream.rangeClosed(0, 1).boxed().collect(Collectors.toSet());
        HashMap<String, Set<Integer>> notations = new HashMap<>();

        notations.put("number", number_notation_range);
        notations.put("comment", comment_notation_range);
        notations.put("binary", binary_notaion_range);

        return notations;
    }
}
