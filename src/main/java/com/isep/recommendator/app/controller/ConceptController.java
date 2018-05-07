package com.isep.recommendator.app.controller;

import com.isep.recommendator.app.model.Concept;
import com.isep.recommendator.app.service.ConceptService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK")
    })
    public ResponseEntity<?> getAll() {

        HttpHeaders resp_headers = new HttpHeaders();
        List<Concept> concepts = conceptService.getAll();
        return new ResponseEntity<>(concepts, resp_headers, HttpStatus.OK);

    }


    @GetMapping("/{id}")
    @ApiOperation(value = "Get a concept by id [PUBLIC]", response = Concept.class)
    public ResponseEntity<?> getById(@PathVariable(value = "id") Long id) {
        HttpHeaders resp_headers = new HttpHeaders();
        Concept concept = conceptService.get(id);

        ResponseEntity<?> resp = concept != null ?
                new ResponseEntity<>(concept, resp_headers, HttpStatus.OK) :
                new ResponseEntity<>("no concept found with id " + id, resp_headers, HttpStatus.NOT_FOUND);

        return resp;
    }


    @PostMapping("")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ApiOperation(value = "Create a concept [ADMIN]", notes="should be admin" ,response = Concept.class)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> create(@RequestParam("name") String name) {
        HttpHeaders resp_headers = new HttpHeaders();
        Concept concept = conceptService.create(name);
        return concept != null ?
                new ResponseEntity<>(concept, resp_headers, HttpStatus.CREATED) :
                new ResponseEntity<>("concept with name " + name + " already exist", resp_headers, HttpStatus.BAD_REQUEST);


    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Delete a concept [ADMIN]", notes="should be admin")
    public ResponseEntity<?> deleteById(@PathVariable(value = "id") Long id){
        HttpHeaders resp_headers = new HttpHeaders();

        Concept concept = conceptService.get(id);
        if (concept == null)
            return new ResponseEntity<>("no concept found with id " + id, resp_headers, HttpStatus.NOT_FOUND);

        conceptService.delete(concept);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ApiOperation(value = "Update a concept [ADMIN]", notes="should be admin", response = Concept.class)
    public ResponseEntity<?> updateById(@PathVariable(value = "id") Long id, @RequestParam("name") String name){
        HttpHeaders resp_headers = new HttpHeaders();

        Concept concept = conceptService.get(id);
        if (concept == null)
            return new ResponseEntity<>("no concept found with id " + id, resp_headers, HttpStatus.NOT_FOUND);

        concept = conceptService.update(concept, name);

        return concept != null ?
                new ResponseEntity<>(concept, resp_headers, HttpStatus.OK) :
                new ResponseEntity<>("concept with name " + name + " already exist", resp_headers, HttpStatus.BAD_REQUEST);
    }
}