package com.isep.recommendator.security;

import com.isep.recommendator.app.Application;
import com.isep.recommendator.app.model.User;
import com.isep.recommendator.app.repository.UserRepository;
import com.isep.recommendator.app.service.UserService;
import com.isep.recommendator.security.config.WebSecurityConfig;
import com.isep.recommendator.security.service.TokenService;
import io.jsonwebtoken.Claims;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;

import static org.springframework.test.util.AssertionErrors.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class, WebSecurityConfig.class})
@WebAppConfiguration
public class TokenServiceTest {


    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private TokenService tokenService;


    @Before
    public void before() {
        this.userRepo.deleteAllInBatch();
    }

    @Test
    public void tokenProcess_user(){
        String email = "bob@email.com";
        String password = "password";

        User user = userService.createUser(email, password,false);
        String token = tokenService.buildToken(user);
        assertTrue("it should generate a token", token.getClass() == String.class && token.length() > 10);

        Claims claims = tokenService.parseToken(token);
        ArrayList<String> roles = (ArrayList<String>) claims.get("roles");
        assertTrue("it should have the good username attached", claims.getSubject().equals(email));
        assertTrue("it should have only one role", roles.size() == 1);
        assertTrue("it should be the USER role", roles.get(0).equals("USER"));
    }

    @Test
    public void tokenProcess_admin(){
        String email = "admin@email.com";
        String password = "password";

        User user = userService.createUser(email, password,true);
        String token = tokenService.buildToken(user);
        assertTrue("it should generate a token", token.getClass() == String.class && token.length() > 10);

        Claims claims = tokenService.parseToken(token);
        ArrayList<String> roles = (ArrayList<String>) claims.get("roles");
        assertTrue("it should have the good username attached", claims.getSubject().equals(email));
        assertTrue("it should have two roles", roles.size() == 2);
        assertTrue("it should contains USER role", roles.indexOf("USER") != -1);
        assertTrue("it should contains ADMIN role", roles.indexOf("ADMIN") != -1);
    }
}
