package com.isep.recommendator.app.controller;

import com.isep.recommendator.app.model.Concept;
import com.isep.recommendator.app.model.Module;
import com.isep.recommendator.app.repository.ModuleRepository;
import com.isep.recommendator.app.service.ConceptService;
import com.isep.recommendator.app.service.ModuleService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/modules")
@Api(value="/modules", description="All endpoints about modules")
public class ModuleController {

    private final ModuleRepository moduleRepo;
    private final ModuleService moduleService;
    private final ConceptService conceptService;

    @Autowired
    public ModuleController(ModuleRepository moduleRepo, ModuleService moduleService, ConceptService conceptService){
        this.moduleRepo = moduleRepo;
        this.moduleService = moduleService;
        this.conceptService = conceptService;
    }


    @GetMapping("")
    @ApiOperation(value = "Get all modules [PUBLIC]", response = Module.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK")
    })
    public ResponseEntity<?> getAll() {

        HttpHeaders resp_headers = new HttpHeaders();
        List<Module> modules = moduleRepo.findAll();
        return new ResponseEntity<>(modules, resp_headers, HttpStatus.OK);

    }


    @GetMapping("/{id}")
    @ApiOperation(value = "Get a module by id [PUBLIC]", response = Module.class)
    public ResponseEntity<?> getById(@PathVariable(value = "id") Long id) {
        HttpHeaders resp_headers = new HttpHeaders();
        Module module = moduleService.get(id);

        ResponseEntity<?> resp = module != null ?
                new ResponseEntity<>(module, resp_headers, HttpStatus.OK) :
                new ResponseEntity<>("no module found with id " + id, resp_headers, HttpStatus.NOT_FOUND);

        return resp;
    }


    @PostMapping("")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ApiOperation(value = "Create a module [ADMIN]", notes="should be admin" ,response = Module.class)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> create(@RequestParam("name") String name, @RequestParam("description") String description) {
        @Valid Module module = new Module(name, description);
        HttpHeaders resp_headers = new HttpHeaders();
        moduleRepo.save(module);
        return new ResponseEntity<>(module, resp_headers, HttpStatus.CREATED);
    }

    @PostMapping("/{id}/concepts")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ApiOperation(value = "add a concept to a module [ADMIN]", notes="should be admin" ,response = Module.class)
    public ResponseEntity<?> addConcept(@PathVariable(value = "id") Long module_id, @RequestParam("concept_id") Long concept_id) {
        HttpHeaders resp_headers = new HttpHeaders();

        Module module = moduleService.get(module_id);
        if (module == null)
            return new ResponseEntity<>("no module found with id " + module_id, resp_headers, HttpStatus.NOT_FOUND);

        Concept concept = conceptService.get(concept_id);
        if (concept == null)
            return new ResponseEntity<>("no concept found with id " + concept_id, resp_headers, HttpStatus.BAD_REQUEST);

        module = moduleService.addConcept(module, concept);

        return new ResponseEntity<>(module, resp_headers, HttpStatus.OK);
    }
}