package com.isep.recommendator.app.controller;

import com.isep.recommendator.app.model.History;
import com.isep.recommendator.app.model.User;
import com.isep.recommendator.app.service.HistoryService;
import com.isep.recommendator.app.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/histories")
@Api(value="/histories", description="All endpoints about histories")
public class HistoryController {

  private final HistoryService historyService;
  private final UserService userService;

  @Autowired
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

  @GetMapping("/self")
  @ApiOperation(value = "Get the histories of the currently logged user [PRIVATE]", response = History.class, responseContainer = "List")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("hasAuthority('USER')")
  public List<History> getSelfHistories(Principal principal){
    User currentUser = userService.getCurrentUser(principal);
    return historyService.getAllByUser(currentUser.getId());
  }

}
