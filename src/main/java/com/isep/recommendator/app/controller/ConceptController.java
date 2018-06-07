package com.isep.recommendator.app.controller;

import com.isep.recommendator.app.handler.BadRequestException;
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
        return concept;
    }


    @PostMapping("")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ApiOperation(value = "Create a concept [ADMIN]", notes="should be admin" ,response = Concept.class)
    @ResponseStatus(HttpStatus.CREATED)
    public Concept create(@RequestParam("name") String name) throws BadRequestException {
        Concept concept = conceptService.create(name);
        return concept;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ApiOperation(value = "Delete a concept [ADMIN]", notes="should be admin")
    public Concept deleteById(@PathVariable(value = "id") Long id){
        Concept concept = conceptService.get(id);
        return conceptService.delete(concept);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ApiOperation(value = "Update a concept [ADMIN]", notes="should be admin", response = Concept.class)
    public Concept updateById(@PathVariable(value = "id") Long id, @RequestParam("name") String name) throws BadRequestException {
        Concept concept = conceptService.get(id);
        concept = conceptService.update(concept, name);
        return concept;
    }
}