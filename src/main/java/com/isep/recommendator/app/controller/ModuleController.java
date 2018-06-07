package com.isep.recommendator.app.controller;

import com.isep.recommendator.app.handler.BadRequestException;
import com.isep.recommendator.app.model.Concept;
import com.isep.recommendator.app.model.Module;
import com.isep.recommendator.app.service.ConceptService;
import com.isep.recommendator.app.service.ModuleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/modules")
@Api(value = "/modules", description = "All endpoints about modules")
public class ModuleController {
    private final ModuleService  moduleService;
    private final ConceptService conceptService;


    @Autowired
    public ModuleController(ModuleService moduleService, ConceptService conceptService){
        this.moduleService = moduleService;
        this.conceptService = conceptService;
    }


    @GetMapping("")
    @ApiOperation(value = "Get all modules [PUBLIC]", response = Module.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK")
    })
    @ResponseStatus(HttpStatus.OK)
    public List<Module> getAll() {

       return moduleService.getAll();
    }


    @GetMapping("/{id}")
    @ApiOperation(value = "Get a module by id [PUBLIC]", response = Module.class)
    @ResponseStatus(HttpStatus.OK)
    public Module getById(@PathVariable(value = "id") Long id) {
        Module module = moduleService.get(id);
        return module;
    }


    @PostMapping("")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ApiOperation(value = "Create a module [ADMIN]", notes = "should be admin", response = Module.class)
    @ResponseStatus(HttpStatus.CREATED)
    public Module create(@RequestParam("name") String name, @RequestParam("description") String description) {
        Module module = moduleService.create(name, description);
        return module;
    }

    @PostMapping("/{id}/concepts")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ApiOperation(value = "add a concept to a module [ADMIN]", notes = "should be admin", response = Module.class)
    @ResponseStatus(HttpStatus.OK)
    public Module addConcept(@PathVariable(value = "id") Long module_id, @RequestParam("concept_id") Long concept_id) {
        Module module = moduleService.get(module_id);
        Concept concept = conceptService.get(concept_id);
        module = moduleService.addConcept(module, concept);
        return module;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ApiOperation(value = "Delete a module [ADMIN]", notes="should be admin")
    public Module deleteById(@PathVariable(value = "id") Long id){
        Module module = moduleService.get(id);
        return moduleService.delete(module);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ApiOperation(value = "Update a module [ADMIN]", notes="should be admin", response = Module.class)
    public Module updateById(@PathVariable(value = "id") Long id, @RequestParam(value = "name", required = false) String name, @RequestParam(value = "description", required = false) String description) throws BadRequestException {
        Module module = moduleService.get(id);
        String new_name = name == null ? module.getName() : name;
        String new_description = description == null ? module.getDescription() : description;
        module = moduleService.update(module, new_name, new_description);
        return module;
    }

    @DeleteMapping("/{id}/concepts/{concept_id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ApiOperation(value = "remove a concept from a module [ADMIN]", notes = "should be admin", response = Module.class)
    @ResponseStatus(HttpStatus.OK)
    public Module removeConcept(@PathVariable(value = "id") Long module_id, @PathVariable("concept_id") Long concept_id) throws BadRequestException {
        Module module = moduleService.get(module_id);
        Concept concept = conceptService.get(concept_id);
        return moduleService.removeConcept(module, concept);
    }
}