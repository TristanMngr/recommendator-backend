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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/jobs")
@Api(value="/jobs", description="All endpoints about jobs")
public class JobController {
    private JobRepository jobRepository;
    private JobService jobService;

    @Autowired
    public JobController(JobRepository jobRepository, JobService jobService) {
        this.jobRepository = jobRepository;
        this.jobService = jobService;
    }

    @GetMapping("")
    @ApiOperation(value = "Get all jobs [PUBLIC]", response = Job.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK")
    })
    public ResponseEntity<?> getAll() {
        HttpHeaders resp_headers = new HttpHeaders();
        List<Job>   jobs         = jobRepository.findAll();
        return new ResponseEntity<>(jobs, resp_headers, HttpStatus.OK);
    }
}
