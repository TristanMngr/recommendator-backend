package com.isep.recommendator.app.controller;


import com.isep.recommendator.app.handler.BadRequestException;
import com.isep.recommendator.app.model.Module;
import com.isep.recommendator.app.model.Requirement;
import com.isep.recommendator.app.service.RequirementService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/requirements")
@Api(value = "/requirements", description = "All endpoints about requirement")
public class RequirementController {
    private final RequirementService requirementService;

    @Autowired
    public RequirementController(RequirementService requirementService) {
        this.requirementService = requirementService;
    }


    @GetMapping("")
    @ApiOperation(value = "Get all requirement [PUBLIC]", response = Requirement.class, responseContainer = "List")
    @ResponseStatus(HttpStatus.OK)
    public List<Requirement> getAll() {
        List<Requirement> concepts = requirementService.getAll();
        return concepts;
    }


    @PostMapping("")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ApiOperation(value = "Create a requirement [ADMIN]", notes = "should be admin", response = Module.class)
    @ResponseStatus(HttpStatus.CREATED)
    // dans le front il faudra mettre une note selon le type, si note classique 0 à 20, 0 à 4, 0 à 1
    public Requirement create(@RequestParam("concept_id") Long conceptId,
                              @RequestParam("note_type") String note_type,
                              @RequestParam("note") Integer note,
                              @RequestParam(value = "mooc", required = false) String mooc,
                              @RequestParam(value = "question", required = false) String question) throws BadRequestException {
        Requirement requirement = requirementService.create(conceptId, note_type, note, mooc, question);
        return requirement;
    }


    @DeleteMapping("/{requirement_id}")
    @ApiOperation(value = "Delete requirement [ADMIN]", response = Requirement.class)
    @PreAuthorize("hasAuthority('ADMIN')")
    public Requirement destroy(@PathVariable("requirement_id") Long requirementId) {
        return this.requirementService.destroy(requirementId);
    }


    @PutMapping("/{requirement_id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ApiOperation(value = "Update a requirement [ADMIN]", notes = "should be admin", response = Requirement.class)
    public Requirement updateById(@PathVariable(value = "requirement_id") Long requirement_id,
                                  @RequestParam(value = "note_type", required = false) String noteType,
                                  @RequestParam(value = "note", required = false) Integer note,
                                  @RequestParam(value = "mooc", required = false) String mooc,
                                  @RequestParam(value = "question", required = false) String question) throws BadRequestException {

        return requirementService.update(requirement_id, noteType, note, mooc, question);

    }
}
