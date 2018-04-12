package com.isep.recommendator.controller;

import com.isep.recommendator.model.Module;
import com.isep.recommendator.repository.ModuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/modules")
public class ModuleController {

    @Autowired
    ModuleRepository moduleRepo;


    @GetMapping("")
    // get all modules
    public ResponseEntity<?> getAll() {

        HttpHeaders resp_headers = new HttpHeaders();
        List<Module> modules = moduleRepo.findAll();
        return new ResponseEntity<>(modules, resp_headers, HttpStatus.OK);

    }


    @GetMapping("/{id}")
    // get the module with the given id
    public ResponseEntity<?> getById(@PathVariable(value = "id") Long id) {

        HttpHeaders resp_headers = new HttpHeaders();

        Optional module = moduleRepo.findById(id);
        if (module.isPresent())
            return new ResponseEntity<>(module, resp_headers, HttpStatus.OK);
        else
            return new ResponseEntity<>("no module found with id " + id, resp_headers, HttpStatus.NOT_FOUND);
    }


    @PostMapping("")
    // create a new module
    public ResponseEntity<?> create(@Valid Module module) {
        HttpHeaders resp_headers = new HttpHeaders();
        moduleRepo.save(module);
        return new ResponseEntity<>(module, resp_headers, HttpStatus.CREATED);
    }
}