package com.isep.recommendator.app.controller;

import com.isep.recommendator.app.model.Concept;
import com.isep.recommendator.app.model.Module;
import com.isep.recommendator.app.model.Speciality;
import com.isep.recommendator.app.repository.ModuleRepository;
import com.isep.recommendator.app.repository.SpecialityRepository;
import com.isep.recommendator.app.service.ConceptService;
import com.isep.recommendator.app.service.SpecialityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/specialities")
@Api(value="/specialities", description="All endpoints about specialities")
public class SpecialityController {
    private SpecialityRepository specialityRepository;
    private SpecialityService specialityService;
    private ConceptService conceptService;

    @Autowired
    public SpecialityController(SpecialityRepository specialityRepository, SpecialityService specialityService,
                                ConceptService conceptService) {
        this.specialityRepository = specialityRepository;
        this.specialityService = specialityService;
        this.conceptService = conceptService;
    }

    @GetMapping("")
    @ApiOperation(value = "Get all specialities [PUBLIC]", response = Speciality.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK")
    })
    @ResponseStatus(HttpStatus.OK)
    public List<Speciality> getAll() {
        return specialityRepository.findAll();
    }

    @GetMapping("/{concept_ids}/concepts")
    @ApiOperation( value = "[FORM] Get an ordered list of speciality from a list of concepts [USER]",
            response=Speciality.class,
            responseContainer = "List")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('USER')")
    public HashMap<?,?> getSpecialitiesFromConcepts(@PathVariable List<Long> concept_ids){
    return specialityService.getSpecialitiesByConceptsIdsWithMatching(concept_ids); // TODO trouver les concepts qui sont dans chacune d'elles
    }
}
