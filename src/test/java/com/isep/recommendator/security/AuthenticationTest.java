package com.isep.recommendator.security;

import com.isep.recommendator.app.Application;
import com.isep.recommendator.app.model.User;
import com.isep.recommendator.app.repository.UserRepository;
import com.isep.recommendator.app.service.UserService;
import com.isep.recommendator.security.config.TokenProperties;
import com.isep.recommendator.security.config.WebSecurityConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.Charset;

import static org.hamcrest.Matchers.isA;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class, WebSecurityConfig.class})
@WebAppConfiguration
public class AuthenticationTest {

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));


    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepo;


    @Autowired
    private WebApplicationContext webApplicationContext;


    private final String AUTH_URL = TokenProperties.getAuthUrl();

    @Before
    public void before() {
        this.mockMvc = webAppContextSetup(webApplicationContext)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();

        this.userRepo.deleteAllInBatch();
    }

    @Test
    // [POST] /modules - all params, successfully created
    public void auth_credentialsOK() throws Exception {
        String email = "user@email.com";
        String password = "password";
        User user = userService.createUser(email, password, false);

        mockMvc.perform(post(AUTH_URL)
                .contentType(contentType)
                .param("email", email)
                .param("password", password))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", isA(String.class))
                );
    }

    @Test
    public void auth_badCredentials() throws Exception {
        String email = "user@email.com";
        String password = "password";
        User user = userService.createUser(email, password, false);

        mockMvc.perform(post(AUTH_URL)
                .contentType(contentType)
                .param("email", email)
                .param("password", "thisisawrongpassword"))
                .andExpect(status().is(401))
                .andExpect(jsonPath("$.token").doesNotExist())
                .andExpect(jsonPath("$.error", isA(String.class))
                );
    }
}
