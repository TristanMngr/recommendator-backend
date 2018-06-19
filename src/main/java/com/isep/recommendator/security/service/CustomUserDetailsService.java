package com.isep.recommendator.security.service;

import com.isep.recommendator.app.model.User;
import com.isep.recommendator.app.service.LDAPaccess;
import com.isep.recommendator.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Autowired
    public CustomUserDetailsService(UserService userService){
        this.userService = userService;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByUsername(username);

        if (user == null)
            throw new UsernameNotFoundException("username not found");

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        grantedAuthorities.add(new SimpleGrantedAuthority("USER"));
        if (user.isAdmin())
            grantedAuthorities.add(new SimpleGrantedAuthority("ADMIN"));

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), grantedAuthorities);
    }

    public UserDetails loadUserByUsername(String username, String password) {
        User user = userService.findByUsername(username);
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();

        // ldap
        if (user == null) {
            LDAPaccess access = new LDAPaccess();
            try {
                boolean isAdmin = false;
                User userLdap = access.LDAPget(username, password); // remplacez login par la variable qui contient le login, et mdp par la variable qui contient le mot de passe
                isAdmin = !userLdap.getEmployeeType().equals("eleve");
                user = userService.createUser(username, password, isAdmin);
            } catch(UsernameNotFoundException e) {
                throw new UsernameNotFoundException("Username not found");
            }
        }

        grantedAuthorities.add(new SimpleGrantedAuthority("USER"));
        if (user.isAdmin())
            grantedAuthorities.add(new SimpleGrantedAuthority("ADMIN"));

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), grantedAuthorities);
    }
}
