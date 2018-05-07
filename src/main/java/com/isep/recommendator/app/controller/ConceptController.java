package com.isep.recommendator.app.controller;

import com.isep.recommendator.app.handler.BadRequestException;
import com.isep.recommendator.app.handler.ResourceNotFoundException;
import com.isep.recommendator.app.model.Concept;
import com.isep.recommendator.app.service.ConceptService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/concepts")
@Api(value="/concepts", description="All endpoints about concepts")
public class ConceptController {

    private final ConceptService conceptService;

    @Autowired
    public ConceptController(ConceptService conceptService){
        this.conceptService = conceptService;
    }


    @GetMapping("")
    @ApiOperation(value = "Get all concepts [PUBLIC]", response = Concept.class, responseContainer = "List")
    @ResponseStatus(HttpStatus.OK)
    public List<Concept> getAll() {
        List<Concept> concepts = conceptService.getAll();
        return concepts;
    }


    @GetMapping("/{id}")
    @ApiOperation(value = "Get a concept by id [PUBLIC]", response = Concept.class)
    public Concept getById(@PathVariable(value = "id") Long id) {
        Concept concept = conceptService.get(id);

        if (concept == null)
            throw new ResourceNotFoundException("no concept found with id " + id);

        return concept;
    }


    @PostMapping("")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ApiOperation(value = "Create a concept [ADMIN]", notes="should be admin" ,response = Concept.class)
    @ResponseStatus(HttpStatus.CREATED)
    public Concept create(@RequestParam("name") String name) throws BadRequestException {
        Concept concept = conceptService.create(name);

        if (concept == null)
            throw new BadRequestException("concept with name " + name + " already exist");

        return concept;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ApiOperation(value = "Delete a concept [ADMIN]", notes="should be admin")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable(value = "id") Long id){
        Concept concept = conceptService.get(id);
        if (concept == null)
           throw new ResourceNotFoundException("no concept found with id " + id);

        conceptService.delete(concept);
        return;
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ApiOperation(value = "Update a concept [ADMIN]", notes="should be admin", response = Concept.class)
    public Concept updateById(@PathVariable(value = "id") Long id, @RequestParam("name") String name) throws BadRequestException {
        Concept concept = conceptService.get(id);
        if (concept == null)
            throw new ResourceNotFoundException("no concept found with id " + id);

        concept = conceptService.update(concept, name);

        if (concept == null)
            throw new BadRequestException("concept with name " + name + " already exist");

        return concept;
    }
}