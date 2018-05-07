package com.isep.recommendator.app.controller;

import com.isep.recommendator.app.model.User;
import com.isep.recommendator.app.repository.UserRepository;
import com.isep.recommendator.app.service.UserService;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepo;
    private final UserService userService;

    @Autowired
    public UserController(UserRepository userRepo, UserService userService){
        this.userRepo = userRepo;
        this.userService = userService;
    }

    @PostMapping("/register")
    // create a new user
    public ResponseEntity<?> create(@RequestParam("email") String email, @RequestParam("password") String password) {
        HttpHeaders resp_headers = new HttpHeaders();

        if (userRepo.findByEmail(email) != null)
            return new ResponseEntity<>("email already used", resp_headers, HttpStatus.BAD_REQUEST);

        User user = userService.register(email, password);
        return new ResponseEntity<>(user, resp_headers, HttpStatus.CREATED);
    }

}