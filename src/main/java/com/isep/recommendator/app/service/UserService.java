package com.isep.recommendator.app.service;

import com.isep.recommendator.app.model.User;
import com.isep.recommendator.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

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

    public User register(String email, String password){
        User user = new User(email, bCryptPasswordEncoder.encode(password), false);
        userRepo.save(user);
        return user;
    }
}
