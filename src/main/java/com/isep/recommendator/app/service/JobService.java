package com.isep.recommendator.app.service;

import com.isep.recommendator.app.handler.BadRequestException;
import com.isep.recommendator.app.handler.CustomValidationException;
import com.isep.recommendator.app.handler.ResourceNotFoundException;
import com.isep.recommendator.app.model.Job;
import com.isep.recommendator.app.model.Speciality;
import com.isep.recommendator.app.repository.JobRepository;
import com.isep.recommendator.app.repository.SpecialityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
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

    public Job create(String name, String description) throws BadRequestException {
        jobAlreadyExist(name);

        try {
            @Valid Job job = new Job(name, description) ;
            return jobRepository.save(job);
        } catch (ConstraintViolationException e) {
            throw new CustomValidationException(e);
        }
    }

    public Job destroy(Long jobId) {
        Job job = jobFound(jobId);

        jobRepository.delete(job);
        return job;
    }

    public Job getJob(Long jobId) throws ResourceNotFoundException {
        Job job = jobFound(jobId);

        return job;
    }

    public List<Speciality> getSpecialitiesByJobsIds(List<Long> jobsIds) throws ResourceNotFoundException {
        List<Speciality> jobs = specialityRepository.findByJobsIds(jobsIds);

        if (jobs.isEmpty()) {
            throw new ResourceNotFoundException("No speciality for theses jobsIds " + jobsIds);
        }

        return jobs;
    }

    // Custom errors messages

    public void jobAlreadyExist(String name) throws BadRequestException {
        if (jobRepository.findByName(name) != null) {
            throw new BadRequestException("Job with name " + name + " already exist");
        }
    }

    public Job jobFound(Long jobId) throws ResourceNotFoundException {
        Optional<Job> job = jobRepository.findById(jobId);

        if (!job.isPresent())
            throw new ResourceNotFoundException("Job with id " + jobId + " not found");

        return job.get();
    }

    public Job update(Job job, String new_name, String new_desc) throws BadRequestException{
        if (!job.getName().equals(new_name)) {
            if (!jobRepository.findListByName(new_name).isEmpty())
                throw new BadRequestException("job with name " + new_name + " already exist");
            job.setName(new_name);
        }
        if (!job.getDescription().equals(new_desc))
            job.setDescription(new_desc);

        return jobRepository.save(job);
    }
}
