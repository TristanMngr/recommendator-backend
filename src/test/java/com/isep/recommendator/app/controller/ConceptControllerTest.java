package com.isep.recommendator.app.controller;

import com.isep.recommendator.app.Application;
import com.isep.recommendator.app.model.Concept;
import com.isep.recommendator.app.repository.ConceptRepository;
import com.isep.recommendator.app.service.ConceptService;
import com.isep.recommendator.security.config.WebSecurityConfig;
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

import static org.hamcrest.Matchers.is;
import static org.springframework.test.util.AssertionErrors.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;


@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class, WebSecurityConfig.class})
@WebAppConfiguration
public class ConceptControllerTest {

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;

    @Autowired
    private ConceptRepository conceptRepo;

    @Autowired
    private ConceptService conceptService;

    @Autowired
    private WebApplicationContext webApplicationContext;


    @Before
    public void before() {
        this.mockMvc = webAppContextSetup(webApplicationContext)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
        this.conceptRepo.deleteAllInBatch();
    }

    @Test
    // [GET] /concepts
    public void getAll() throws Exception {
        Concept concept = this.conceptRepo.save(new Concept("name"));
        Concept concept_bis = this.conceptRepo.save(new Concept("name2"));

        mockMvc.perform(get("/concepts")
                .contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(concept.getId().intValue())))
                .andExpect(jsonPath("$[0].name", is(concept.getName())))
                .andExpect(jsonPath("$[1].id", is(concept_bis.getId().intValue())))
                .andExpect(jsonPath("$[1].name", is(concept_bis.getName())));
    }

    @Test
    // [GET] /concepts/{id} - no concept with this id
    public void conceptNotFound() throws Exception {
        mockMvc.perform(get("/concepts/1")
                .contentType(contentType))
                .andExpect(status().isNotFound());
    }

    @Test
    // [GET] /concepts/{id} - returning the given concept
    public void getOneConcept() throws Exception {
        Concept concept = this.conceptRepo.save(new Concept("name"));

        mockMvc.perform(get("/concepts/"+concept.getId())
                .contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(concept.getId().intValue())))
                .andExpect(jsonPath("$.name", is(concept.getName())));
    }

    @Test
    @WithMockUser(authorities = {"USER" , "ADMIN"})
    // [POST] /concepts - all params, successfully created
    public void postAllParams_success() throws Exception {
        String name = "nom du concept";

        mockMvc.perform(post("/concepts")
                .contentType(contentType)
                .param("name", name))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(name))
        );

        // la réponse est good, on test maintenant si ça a bien été mis en BDD.
        Concept concept = conceptRepo.findByName(name);
        assertTrue("it should find a concept with this name", concept != null);
    }

    @Test
    @WithMockUser(authorities = {"USER" , "ADMIN"})
    // [POST] /concepts - all params, successfully created
    public void postAllParams_notUnique() throws Exception {
        String name = "nom du concept";
        conceptRepo.save(new Concept(name));

        mockMvc.perform(post("/concepts")
                .contentType(contentType)
                .param("name", name))
                .andExpect(status().isBadRequest()
                );
    }

    @Test
    @WithMockUser(authorities = {"USER"})
    // [POST] /concepts - all params, not admin
    public void postAllParams_forbidden() throws Exception {
        String name = "nom du concept";
        String description = "description du concept";

        mockMvc.perform(post("/concepts")
                .contentType(contentType)
                .param("name", name)
                .param("description", description))
                .andExpect(status().isForbidden()
                );

        // la réponse est good, on test maintenant que rien n'a été persist
        Concept concept = conceptRepo.findByName(name);
        assertTrue("it shouldn't find any concept with this name", concept == null);
    }

    @Test
    @WithMockUser(authorities = {"USER" , "ADMIN"})
    // [POST] /concepts - missing params, nothing created
    public void postMissingParams() throws Exception {
        String name = "nom du concept";

        mockMvc.perform(post("/concepts")
                .contentType(contentType))
                .andExpect(status().isBadRequest());

        // la réponse est good, on test maintenant que rien n'a été persist
        Concept concept = conceptRepo.findByName(name);
        assertTrue("it shouldn't find any concept with this name", concept == null);
    }

    @Test
    @WithMockUser(authorities = {"USER" , "ADMIN"})
    // [DELETE] /concepts/{id} - admin
    public void deleteConcept_OK() throws Exception {
        String name = "nom du concept";
        Concept concept = conceptRepo.save(new Concept(name));

        assertTrue("the db should contain this concept", conceptRepo.findByName(name) != null);

        mockMvc.perform(delete("/concepts/"+concept.getId())
                .contentType(contentType))
                .andExpect(status().isOk());

        assertTrue("the db should not contain this concept anymore", conceptRepo.findByName(name) == null);
    }

    @Test
    @WithMockUser(authorities = {"USER"})
    // [DELETE] /concepts/{id} - no admin
    public void deleteConcept_forbidden() throws Exception {
        String name = "nom du concept";
        Concept concept = conceptRepo.save(new Concept(name));

        mockMvc.perform(delete("/concepts/"+concept.getId())
                .contentType(contentType))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = {"USER", "ADMIN"})
    // [DELETE] /concepts/{id} - concept not found
    public void deleteConcept_notFound() throws Exception {
        mockMvc.perform(delete("/concepts/420")
                .contentType(contentType))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(authorities = {"USER" , "ADMIN"})
    // [PUT] /concepts/{id} - admin
    public void updateConcept_OK() throws Exception {
        String name = "nom du concept";
        String new_name = "nouveau nom";
        Concept concept = conceptRepo.save(new Concept(name));

        assertTrue("the db should contain this concept", conceptRepo.findByName(name) != null);

        mockMvc.perform(put("/concepts/"+concept.getId())
                .contentType(contentType)
                .param("name", new_name))
                .andExpect(status().isOk());

        assertTrue("the concept name should have been updated", conceptService.get(concept.getId()).getName().equals(new_name));
    }

    @Test
    @WithMockUser(authorities = {"USER"})
    // [PUT] /concepts/{id} - no admin
    public void updateConcept_forbidden() throws Exception {
        mockMvc.perform(put("/concepts/1")
                .contentType(contentType)
                .param("name", "blabla"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = {"USER", "ADMIN"})
    // [PUT] /concepts/{id} - no admin
    public void updateConcept_badRequest() throws Exception {
        String name = "nom du concept";
        Concept concept = conceptRepo.save(new Concept(name));

        mockMvc.perform(put("/concepts/1")
                .contentType(contentType))
                .andExpect(status().isBadRequest());

        assertTrue("the concept name shouldn't have been updated", conceptService.get(concept.getId()).getName().equals(name));
    }

}