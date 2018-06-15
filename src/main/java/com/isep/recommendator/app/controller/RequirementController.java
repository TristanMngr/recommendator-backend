package com.isep.recommendator.app.controller;


import com.isep.recommendator.app.model.Concept;
import com.isep.recommendator.app.model.Requirement;
import com.isep.recommendator.app.service.ConceptService;
import com.isep.recommendator.app.service.RequirementService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/requirements")
public class RequirementController {
    private final RequirementService requirementService;

    @Autowired
    public RequirementController(RequirementService requirementService){
        this.requirementService = requirementService;
    }


    @GetMapping("")
    @ApiOperation(value = "Get all requirement [PUBLIC]", response = Requirement.class, responseContainer = "List")
    @ResponseStatus(HttpStatus.OK)
    public List<Requirement> getAll() {
        List<Requirement> concepts = requirementService.getAll();
        return concepts;
    }



}
