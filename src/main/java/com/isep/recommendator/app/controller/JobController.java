package com.isep.recommendator.app.controller;

import com.isep.recommendator.app.model.Job;
import com.isep.recommendator.app.model.Module;
import com.isep.recommendator.app.repository.JobRepository;
import com.isep.recommendator.app.service.JobService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Api(value="/jobs", description="All endpoints about jobs")
public class JobController {
    private JobRepository jobRepository;
    private JobService jobService;

    @Autowired
    public JobController(JobRepository jobRepository, JobService jobService) {
        this.jobRepository = jobRepository;
        this.jobService = jobService;
    }

    @GetMapping("/jobs")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get all jobs [PUBLIC]", response = Job.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK")
    })
    public List<Job> getJobs() {
        System.out.println(jobRepository.findAll());
        return jobRepository.findAll();

    }

    @PostMapping("/jobs")
    @ApiOperation(value = "create job [PUBLIC]", response = Job.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created")
    })
    @ResponseStatus(HttpStatus.CREATED)
    public Job createJob(Job job) {
        return jobRepository.save(job);
    }

}
