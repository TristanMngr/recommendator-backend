package com.isep.recommendator.app.service;

import com.isep.recommendator.app.model.Concept;
import com.isep.recommendator.app.repository.ConceptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Service
public class ConceptService {

    private final ConceptRepository conceptRepo;

    @Autowired
    public ConceptService(ConceptRepository conceptRepo){
        this.conceptRepo = conceptRepo;
    }

    public Concept get(Long id){
        Optional concept = conceptRepo.findById(id);
        if (concept.isPresent())
            return (Concept) concept.get();
        else
            return null;
    }

    public List<Concept> getAll(){
        return conceptRepo.findAll();
    }

    public Concept create(String name){
        if (conceptRepo.findByName(name) != null)
            return null;

        @Valid Concept concept = new Concept(name);
        return conceptRepo.save(concept);
    }

    public Concept save(Concept concept){
        return conceptRepo.save(concept);
    }

}
