package com.isep.recommendator.app.controller;

import com.isep.recommendator.app.handler.BadRequestException;
import com.isep.recommendator.app.model.User;
import com.isep.recommendator.app.repository.UserRepository;
import com.isep.recommendator.app.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepo;
    private final UserService    userService;

    @Autowired
    public UserController(UserRepository userRepo, UserService userService) {
        this.userRepo = userRepo;
        this.userService = userService;
    }

    @PostMapping("/register")
    // create a new user
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@RequestParam("username") String username, @RequestParam("password") String password)
    throws BadRequestException {
        User user = userService.register(username, password);
        return user;
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Get a user by id [PRIVATE]", notes="should be the owner of the profile, or admin", response = User.class)
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('USER')")
    public User getById(@PathVariable(value = "id") Long id, Principal principal) {
        User user = userService.get(id);
        User currentUser = userService.getCurrentUser(principal);

        if (currentUser.isAdmin() || currentUser == user)
            return user;
        else
            throw new AccessDeniedException("Access Denied");
    }

    @GetMapping("/self")
    @ApiOperation(value = "Get the user currently logged [PRIVATE]", response = User.class)
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('USER')")
    public User getSelf(Principal principal){
        return userService.getCurrentUser(principal);
    }

}