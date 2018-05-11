package com.isep.recommendator.app.controller;

import com.isep.recommendator.app.model.Job;
import com.isep.recommendator.app.model.Speciality;
import com.isep.recommendator.app.repository.JobRepository;
import com.isep.recommendator.app.service.JobService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/jobs")
@Api(value = "/jobs", description = "All endpoints about jobs")
public class JobController {
    private JobService jobService;

    @Autowired
    public JobController(JobService jobService) {
        this.jobService = jobService;
    }


    @GetMapping("")
    @ApiOperation(value = "Get all jobs [USER]", response = Job.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK")
    })
    @ResponseStatus(HttpStatus.OK)
    public List<Job> getAll() {
        return jobService.getAll();

    }


    @PostMapping("")
    @ApiOperation(value = "create job [ADMIN]", response = Job.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created")
    })
    @ResponseStatus(HttpStatus.CREATED)
    public Job create(Job job) {
        return jobService.create(job);
    }


    @ApiOperation(value = "get a specific job [PUBLIC]", response = Job.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok")
    })
    @GetMapping("/{job_id}")
    @ResponseStatus(HttpStatus.OK)
    public Job getJob(@PathVariable(value = "job_id") Long jobId) {
        return jobService.getJob(jobId);
    }


    @ApiOperation(value = "delete a specific job [ADMIN]", response = Job.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok")
    })
    @DeleteMapping("/{job_id}")
    @ResponseStatus(HttpStatus.OK)
    public Job destroy(@PathVariable(value = "job_id") Long jobId) {
        return jobService.destroy(jobId);
    }


    @ApiOperation(value = "Get specialities from a specific Job [ALL]", response = Job.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok")

    })
    @GetMapping("/{job_ids}/specialities")
    @ResponseStatus(HttpStatus.OK)
    public List<Speciality> getSpecialities(@PathVariable(value = "job_ids") List<Long> jobIds) {
        return jobService.getSpecialitiesByJobsIds(jobIds);
    }
}
