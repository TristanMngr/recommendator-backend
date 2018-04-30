package com.isep.recommendator.app.controller;

import com.isep.recommendator.app.model.User;
import com.isep.recommendator.app.repository.UserRepository;
import com.isep.recommendator.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepo;
    private final UserService userService;
    private AuthenticationManager authenticationManager;

    @Autowired
    public UserController(UserRepository userRepo, UserService userService){
        this.userRepo = userRepo;
        this.userService = userService;
    }

    @PostMapping("/register")
    // create a new user
    public ResponseEntity<?> create(@RequestParam("email") String email, @RequestParam("password") String password) {
        HttpHeaders resp_headers = new HttpHeaders();
        User user = userService.register(email, password);
        return new ResponseEntity<>(user, resp_headers, HttpStatus.CREATED);
    }

//    @PostMapping("/auth")
//    // create a new user
//    public ResponseEntity<?> connect(@RequestParam("email") String email, @RequestParam("password") String password) { HttpHeaders resp_headers = new HttpHeaders();
////       User user = userRepo.findByEmail(email);
////       if (user.getPassword().equals(password)){
////           return new ResponseEntity<>("connected", resp_headers, HttpStatus.OK);
////       }
////       else {
////           return new ResponseEntity<>("UNconnected", resp_headers, HttpStatus.OK);
////       }
//        Authentication auth = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        email,
//                        password,
//                        new ArrayList<>())
//        );
//        return new ResponseEntity<>(auth, resp_headers, HttpStatus.OK);
//    }
}