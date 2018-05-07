package com.isep.recommendator.app.service;

import com.isep.recommendator.app.handler.BadRequestException;
import com.isep.recommendator.app.handler.CustomValidationException;
import com.isep.recommendator.app.model.User;
import com.isep.recommendator.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.security.Principal;

@Service
public class UserService  {

    private final UserRepository userRepo;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserService(UserRepository userRepo, BCryptPasswordEncoder bCryptPasswordEncoder){
        this.userRepo = userRepo;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public User findByEmail(String email){
        return userRepo.findByEmail(email);
    }

    public User register(String email, String password) throws BadRequestException {
        if (userRepo.findByEmail(email) != null)
            throw new BadRequestException("email not found");

        return createUser(email, password, false);
    }

    public User createUser(String email, String password, boolean isAdmin){
        try {
            @Valid User user = new User(email, bCryptPasswordEncoder.encode(password), isAdmin);
            userRepo.save(user);
            return user;
        } catch (ConstraintViolationException e){
            throw new CustomValidationException(e);
        }
    }

    //used the user who made the request in a controller
    public User getCurrentUser(Principal principal){
        return userRepo.findByEmail(principal.getName());
    }

}
