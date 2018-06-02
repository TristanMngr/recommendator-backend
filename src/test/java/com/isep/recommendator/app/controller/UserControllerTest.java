package com.isep.recommendator.app.controller;

import com.isep.recommendator.app.Application;
import com.isep.recommendator.app.model.Concept;
import com.isep.recommendator.app.model.Module;
import com.isep.recommendator.app.model.User;
import com.isep.recommendator.app.repository.ConceptRepository;
import com.isep.recommendator.app.repository.ModuleRepository;
import com.isep.recommendator.app.repository.UserRepository;
import com.isep.recommendator.app.service.ConceptService;
import com.isep.recommendator.app.service.ModuleService;
import com.isep.recommendator.app.service.UserService;
import com.isep.recommendator.security.config.WebSecurityConfig;
import com.isep.recommendator.security.service.TokenService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.Charset;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertFalse;
import static org.springframework.test.util.AssertionErrors.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class, WebSecurityConfig.class})
@WebAppConfiguration
public class UserControllerTest {

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private UserService userService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private WebApplicationContext webApplicationContext;



    @Before
    public void before() {
        this.mockMvc = webAppContextSetup(webApplicationContext)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
        this.userRepo.deleteAllInBatch();
    }

    @Test
    public void getUser_forbidden() throws Exception {
        User user_one = userService.createUser("user_one", "user", false);
        User user_two = userService.createUser("user_two", "user", false);

        String token = tokenService.buildToken(user_two);

        mockMvc.perform(get("/users/"+user_one.getId())
                .header("Authorization", "Bearer " + token)
                .contentType(contentType))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = {"USER" , "ADMIN"})
    public void getUser_admin() throws Exception {
        User user = userService.createUser("user", "user", false);

        mockMvc.perform(get("/users/"+user.getId())
                .contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(user.getId().intValue())))
                .andExpect(jsonPath("$.username", is(user.getUsername())));
    }

    @Test
    public void getUser_OK() throws Exception {
        User user = userService.createUser("user", "user", false);

        String token = tokenService.buildToken(user);
        mockMvc.perform(get("/users/"+user.getId())
                .header("Authorization", "Bearer " + token)
                .contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(user.getId().intValue())))
                .andExpect(jsonPath("$.username", is(user.getUsername())));
    }

    @Test
    public void self() throws Exception {
        User user = userService.createUser("user", "user", false);

        String token = tokenService.buildToken(user);
        mockMvc.perform(get("/users/self")
                .header("Authorization", "Bearer " + token)
                .contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(user.getId().intValue())))
                .andExpect(jsonPath("$.username", is(user.getUsername())));

        mockMvc.perform(get("/users/self")
                .contentType(contentType))
                .andExpect(status().isForbidden());
    }


}