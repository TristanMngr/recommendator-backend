package com.isep.recommendator.app.service;

import com.isep.recommendator.app.handler.BadRequestException;
import com.isep.recommendator.app.handler.CustomValidationException;
import com.isep.recommendator.app.handler.ResourceNotFoundException;
import com.isep.recommendator.app.model.User;
import com.isep.recommendator.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.security.Principal;
import java.util.Optional;

@Service
public class UserService  {

    private final UserRepository userRepo;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserService(UserRepository userRepo, BCryptPasswordEncoder bCryptPasswordEncoder){
        this.userRepo = userRepo;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public User get(Long id){
        Optional user = userRepo.findById(id);
        if (user.isPresent())
            return (User) user.get();
        else
            throw new ResourceNotFoundException("no user found with id " + id);
    }

    public User findByUsername(String username){
        return userRepo.findByUsername(username);
    }

    public User register(String username, String password) throws BadRequestException {
        if (userRepo.findByUsername(username) != null)
            throw new BadRequestException("username not found");

        return createUser(username, password, false);
    }

    public User createUser(String username, String password, boolean isAdmin){
        try {
            @Valid User user = new User(username, bCryptPasswordEncoder.encode(password), isAdmin);
            userRepo.save(user);
            return user;
        } catch (ConstraintViolationException e){
            throw new CustomValidationException(e);
        }
    }

    //used the user who made the request in a controller
    public User getCurrentUser(Principal principal){
        return userRepo.findByUsername(principal.getName());
    }

}
