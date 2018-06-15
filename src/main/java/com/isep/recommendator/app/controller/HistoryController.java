package com.isep.recommendator.app.controller;

import com.isep.recommendator.app.handler.BadRequestException;
import com.isep.recommendator.app.model.Concept;
import com.isep.recommendator.app.model.History;
import com.isep.recommendator.app.model.Module;
import com.isep.recommendator.app.repository.HistoryRepository;
import com.isep.recommendator.app.repository.UserRepository;
import com.isep.recommendator.app.service.ConceptService;
import com.isep.recommendator.app.service.HistoryService;
import com.isep.recommendator.app.service.ModuleService;
import com.isep.recommendator.app.service.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

import io.swagger.annotations.ApiOperation;

public class HistoryController {

  private final HistoryService historyService;
  private final UserService userService;

  public HistoryController(HistoryService historyService, UserService userService){
    this.historyService = historyService;
    this.userService = userService;
  }

  @GetMapping("/{user_id}/{id}")
  @ApiOperation(value = "Get an history by id [PUBLIC]", response = History.class)
  @ResponseStatus(HttpStatus.OK)
  public History getByUser(@PathVariable(value = "user_id") Long user_id, @PathVariable(value = "id") Long id) {
    History history = historyService.getByUser(user_id, id);
    return history;
  }

  @GetMapping("/{user_id}")
  @ApiOperation(value = "Get all histories by user_id [PUBLIC]", response = History.class, responseContainer = "List")
  @ResponseStatus(HttpStatus.OK)
  public List<History> getByUser(@PathVariable(value = "user_id") Long user_id){

    return historyService.getAllByUser(user_id);
  }

  /*@PutMapping("/{id}")
  @ApiOperation(value = "Update an history", response = Module.class)
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
  }*/






}
