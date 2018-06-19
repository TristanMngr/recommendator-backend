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
    return historyService.getByUser(user_id, id);
  }


  @GetMapping("/{user_id}")
  @ApiOperation(value = "Get all histories by user_id [PUBLIC]", response = History.class, responseContainer = "List")
  @ResponseStatus(HttpStatus.OK)
  public List<History> getAllByUser(@PathVariable(value = "user_id") Long user_id){

    return historyService.getAllByUser(user_id);
  }

}
