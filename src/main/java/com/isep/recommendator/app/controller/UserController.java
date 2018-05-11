package com.isep.recommendator.app.controller;

import com.isep.recommendator.app.handler.BadRequestException;
import com.isep.recommendator.app.model.User;
import com.isep.recommendator.app.repository.UserRepository;
import com.isep.recommendator.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
    public User create(@RequestParam("email") String email, @RequestParam("password") String password)
            throws BadRequestException {
        User user = userService.register(email, password);
        return user;
    }

}