package com.isep.recommendator.app.controller;

import com.isep.recommendator.app.Application;
import com.isep.recommendator.app.model.Job;
import com.isep.recommendator.app.model.Speciality;
import com.isep.recommendator.app.repository.JobRepository;
import com.isep.recommendator.app.repository.SpecialityRepository;
import com.isep.recommendator.app.service.JobService;
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
import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class, WebSecurityConfig.class})
@WebAppConfiguration
public class JobControllerTest {
    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private SpecialityRepository specialityRepository;

    @Autowired
    private JobService jobService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void before() {
        this.mockMvc = webAppContextSetup(webApplicationContext)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
        this.jobRepository.deleteAllInBatch();
        this.specialityRepository.deleteAllInBatch();
    }

    @Test
    // [GET] /jobs
    public void getAll() throws Exception {
        Job firstJob  = this.jobRepository.save(new Job("job1", "descriptionJob1"));
        Job secondJob = this.jobRepository.save(new Job("job2", "descriptionJob2"));

        mockMvc.perform(get("/jobs")
                .contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(firstJob.getId().intValue())))
                .andExpect(jsonPath("$[0].name", is(firstJob.getName())))
                .andExpect(jsonPath("$[1].id", is(secondJob.getId().intValue())))
                .andExpect(jsonPath("$[1].name", is(secondJob.getName())));
    }

    @Test
    // [GET] /jobs/{id} - no job with this id
    public void JobNotFound() throws Exception {
        mockMvc.perform(get("/jobs/1")
                .contentType(contentType))
                .andExpect(status().isNotFound());
    }

    @Test
    // [GET] /jobs/{id}
    public void getJob() throws Exception {
        Job job = this.jobRepository.save(new Job("name", "description"));

        mockMvc.perform(get("/jobs/" + job.getId())
                .contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(job.getId().intValue())))
                .andExpect(jsonPath("$.name", is(job.getName())))
                .andExpect(jsonPath("$.description", is(job.getDescription())));
    }

    @Test
    // [DELETE] /jobs/{id}
    @WithMockUser(authorities = {"ADMIN" })
    public void destroy() throws Exception {
        Job  job   = this.jobRepository.save(new Job("name", "description"));
        Long jobId = job.getId();

        mockMvc.perform(delete("/jobs/" + jobId)
                .contentType(contentType))
                .andExpect(status().isOk());

        assertFalse(this.jobRepository.findById(jobId).isPresent());
    }

    @Test
    // [GET] jobs/{job_ids}/specialities
    public void getSpecialities() throws Exception {
        Job firstJob  = this.jobRepository.save(new Job("job1", "job1"));
        Job secondJob = this.jobRepository.save(new Job("job2", "job2"));
        Job thirdJob  = this.jobRepository.save(new Job("job3", "job3"));

        Speciality firstSpecialityFirstJob  = this.specialityRepository.save(new Speciality("speciality1", "speciality1"));
        Speciality secondSpecialityFirstJob = this.specialityRepository.save(new Speciality("speciality2", "speciality2"));
        Speciality thirdSpecialitySecondJob = this.specialityRepository.save(new Speciality("speciality3", "speciality3"));
        Speciality fourthSpecialityThirdJob = this.specialityRepository.save(new Speciality("speciality4", "speciality4"));

        firstJob.getSpecialities().add(firstSpecialityFirstJob);
        firstJob.getSpecialities().add(secondSpecialityFirstJob);
        secondJob.getSpecialities().add(thirdSpecialitySecondJob);
        thirdJob.getSpecialities().add(fourthSpecialityThirdJob);

        firstSpecialityFirstJob.getJobs().add(firstJob);
        secondSpecialityFirstJob.getJobs().add(firstJob);
        thirdSpecialitySecondJob.getJobs().add(secondJob);
        fourthSpecialityThirdJob.getJobs().add(thirdJob);

        this.jobRepository.save(firstJob);
        this.jobRepository.save(secondJob);
        this.jobRepository.save(thirdJob);

        mockMvc.perform(get("/jobs/" + firstJob.getId() + "," + secondJob.getId() + "/specialities")
                .contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].name", isIn(Arrays.asList("speciality1", "speciality2", "speciality3"))))
                .andExpect(jsonPath("$.[1].name", isIn(Arrays.asList("speciality1", "speciality2", "speciality3"))))
                .andExpect(jsonPath("$.[2].name", isIn(Arrays.asList("speciality1", "speciality2", "speciality3"))))
                .andExpect(jsonPath("$.[0].name", not("speciality4")))
                .andExpect(jsonPath("$.[1].name", not("speciality4")))
                .andExpect(jsonPath("$.[2].name", not("speciality4")))
                .andExpect(jsonPath("$", hasSize(3)));

    }


}
