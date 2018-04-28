package com.isep.recommendator.controller;

import com.isep.recommendator.model.Module;
import com.isep.recommendator.repository.ModuleRepository;
import com.isep.recommendator.service.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/modules")
public class ModuleController {

    private final ModuleRepository moduleRepo;
    private final ModuleService moduleService;

    @Autowired
    public ModuleController(ModuleRepository moduleRepo, ModuleService moduleService){
        this.moduleRepo = moduleRepo;
        this.moduleService = moduleService;
    }


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
        Module module = moduleService.get(id);

        ResponseEntity<?> resp = module != null ?
                new ResponseEntity<>(module, resp_headers, HttpStatus.OK) :
                new ResponseEntity<>("no module found with id " + id, resp_headers, HttpStatus.NOT_FOUND);

        return resp;
    }


    @PostMapping("")
    // create a new module
    public ResponseEntity<?> create(@Valid Module module) {
        HttpHeaders resp_headers = new HttpHeaders();
        moduleRepo.save(module);
        return new ResponseEntity<>(module, resp_headers, HttpStatus.CREATED);
    }
}