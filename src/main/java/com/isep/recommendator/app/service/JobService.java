package com.isep.recommendator.app.service;

import com.isep.recommendator.app.handler.ResourceNotFoundException;
import com.isep.recommendator.app.model.Job;
import com.isep.recommendator.app.model.Speciality;
import com.isep.recommendator.app.repository.JobRepository;
import com.isep.recommendator.app.repository.SpecialityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JobService {
    private JobRepository        jobRepository;
    private SpecialityRepository specialityRepository;

    @Autowired
    public JobService(JobRepository jobRepository, SpecialityRepository specialityRepository) {
        this.jobRepository = jobRepository;
        this.specialityRepository = specialityRepository;
    }

    public List<Job> getAll() {
        return jobRepository.findAll();
    }

    public Job create(Job job) {
        return jobRepository.save(job);
    }

    public Job destroy(Long jobId) {
        Optional<Job> job = jobRepository.findById(jobId);

        if (!job.isPresent())
            throw new ResourceNotFoundException("Job with id " + jobId + " not found");

        jobRepository.delete(job.get());

        return job.get();
    }

    public Job getJob(Long jobId) throws ResourceNotFoundException {
        Optional<Job> job = jobRepository.findById(jobId);

        if (!job.isPresent())
            throw new ResourceNotFoundException("Job with id " + jobId + " not found");
        return job.get();
    }

    public List<Speciality> getSpecialitiesByJobsIds(List<Long> jobsIds) throws ResourceNotFoundException {
        List<Speciality> jobs = specialityRepository.findByJobsIds(jobsIds);

        if (jobs.isEmpty()) {
            throw new ResourceNotFoundException("No speciality for theses jobsIds " + jobsIds);
        }

        return jobs;
    }
}
