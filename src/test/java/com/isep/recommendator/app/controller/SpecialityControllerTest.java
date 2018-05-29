package com.isep.recommendator.app.controller;

import com.isep.recommendator.app.Application;
import com.isep.recommendator.app.model.Job;
import com.isep.recommendator.app.model.Module;
import com.isep.recommendator.app.model.Speciality;
import com.isep.recommendator.app.repository.JobRepository;
import com.isep.recommendator.app.repository.ModuleRepository;
import com.isep.recommendator.app.repository.SpecialityRepository;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.Charset;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class, WebSecurityConfig.class})
@WebAppConfiguration
public class SpecialityControllerTest {
    @Autowired
    SpecialityRepository specialityRepository;
    @Autowired
    ModuleRepository moduleRepository;
    @Autowired
    JobRepository jobRepository;
    @Autowired
    WebApplicationContext webApplicationContext;
    private Speciality firstSpeciality;
    private Speciality secondSpeciality;
    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));
    private MockMvc mockMvc;

    @Before
    public void before() {
        this.mockMvc = webAppContextSetup(webApplicationContext)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();

        this.specialityRepository.deleteAllInBatch();
        this.specialityRepository.deleteAllInBatch();

        firstSpeciality = this.specialityRepository.save(new Speciality("speciality1", "descriptionSpeciliaty1"));
        secondSpeciality = this.specialityRepository.save(new Speciality("speciality2", "descriptionSpeciality2"));
    }

    @Test
    // [GET] /specialities
    public void getAll() throws Exception {
        mockMvc.perform(get("/specialities")
                .contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(firstSpeciality.getId().intValue())))
                .andExpect(jsonPath("$[0].name", is(firstSpeciality.getName())))
                .andExpect(jsonPath("$[1].id", is(secondSpeciality.getId().intValue())))
                .andExpect(jsonPath("$[1].name", is(secondSpeciality.getName())));
    }

    @Test
    // [GET] /specialities/{id} - no speciality with this id
    public void SpecialityNotFound() throws Exception {
        mockMvc.perform(get("/specilities/100")
                .contentType(contentType))
                .andExpect(status().isNotFound());
    }

    @Test
    // [GET] /specialities/{id}
    public void getSpeciality() throws Exception {
        mockMvc.perform(get("/specialities/" + firstSpeciality.getId())
                .contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(firstSpeciality.getId().intValue())))
                .andExpect(jsonPath("$.name", is(firstSpeciality.getName())))
                .andExpect(jsonPath("$.description", is(firstSpeciality.getDescription())));
    }

    @Test
    // [DELETE] /specialities/{id}
    @WithMockUser(authorities = {"ADMIN" })
    public void destroy() throws Exception {
        Long specialityId = firstSpeciality.getId();

        mockMvc.perform(delete("/specialities/" + specialityId)
                .contentType(contentType))
                .andExpect(status().isOk());

        assertFalse(this.specialityRepository.findById(specialityId).isPresent());
    }

    //TODO to test
    // [GET] specialities/{specialitiesIds}/modules

    @Test
    // [POST] specialities/{speciality_id}/modules
    // add main module
    @WithMockUser(authorities = {"ADMIN" })
    public void addMainModuleToSpeciality() throws Exception {
        Module firstModule = this.moduleRepository.save(new Module("module1", "module1"));

        mockMvc.perform(post("/specialities/" + firstSpeciality.getId() + "/modules")
                .contentType(contentType)
                .param("module_id", firstModule.getId().toString())
                .param("is_main", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("speciality1")));

        //TODO verifier que la liason est faite

    }

    @Test
    // [POST] specialities/{speciality_id}/modules
    // ad normal module
    @WithMockUser(authorities = {"ADMIN" })
    public void addNormalModuleToSpeciality() throws Exception {
        Module firstModule = this.moduleRepository.save(new Module("module1", "module1"));

        mockMvc.perform(post("/specialities/" + firstSpeciality.getId() + "/modules")
                .contentType(contentType)
                .param("module_id", firstModule.getId().toString())
                .param("is_main", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("speciality1")));

        //la je test avec le speciality 2 pour le même objet module
        mockMvc.perform(post("/specialities/" + secondSpeciality.getId() + "/modules")
                .contentType(contentType)
                .param("module_id", firstModule.getId().toString())
                .param("is_main", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("speciality2")));

        //TODO verifier que la liaison est faite
    }

    @Test
    // [POST] specialities/{speciality_id}/jobs
    // ad jobs
    @WithMockUser(authorities = {"ADMIN" })
    public void addJobToSpeciality() throws Exception {
        Job job = this.jobRepository.save(new Job("job1", "job1"));

        mockMvc.perform(post("/specialities/" + firstSpeciality.getId() + "/jobs")
                .contentType(contentType)
                .param("job_id", job.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("speciality1")));

        mockMvc.perform(post("/specialities/" + secondSpeciality.getId() + "/jobs")
                .contentType(contentType)
                .param("job_id", job.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("speciality2")));


        //TODO verifier que la liaison est faite


        }
}
