package com.isep.recommendator.app.controller;

import com.isep.recommendator.app.handler.BadRequestException;
import com.isep.recommendator.app.handler.ResourceNotFoundException;
import com.isep.recommendator.app.model.Concept;
import com.isep.recommendator.app.model.Module;
import com.isep.recommendator.app.repository.ModuleRepository;
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
@Api(value="/modules", description="All endpoints about modules")
public class ModuleController {

    private final ModuleService moduleService;
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

        if (module == null)
            throw new ResourceNotFoundException("no module found with id " + id);

        return module;
    }


    @PostMapping("")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ApiOperation(value = "Create a module [ADMIN]", notes="should be admin" ,response = Module.class)
    @ResponseStatus(HttpStatus.CREATED)
    public Module create(@RequestParam("name") String name, @RequestParam("description") String description) {
        Module module = moduleService.create(name, description);
        return module;
    }

    @PostMapping("/{id}/concepts")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ApiOperation(value = "add a concept to a module [ADMIN]", notes="should be admin" ,response = Module.class)
    @ResponseStatus(HttpStatus.OK)
    public Module addConcept(@PathVariable(value = "id") Long module_id, @RequestParam("concept_id") Long concept_id)
    throws BadRequestException {
        Module module = moduleService.get(module_id);
        if (module == null)
            throw new ResourceNotFoundException("no module found with id " + module_id);

        Concept concept = conceptService.get(concept_id);
        if (concept == null)
            throw new BadRequestException("no concept found with id " + concept_id);

        module = moduleService.addConcept(module, concept);

        return module;
    }
}