package com.isep.recommendator.app.custom_object;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.isep.recommendator.app.model.Job;
import com.isep.recommendator.app.model.Speciality;

import java.util.ArrayList;
import java.util.List;

public class SpecialityWithMatchingJobs {
    private Speciality speciality;
    private Long id;
    private String name;
    private String description;
    private List<Job>  matching_jobs;
    private List<Job> no_matching_jobs;

    public SpecialityWithMatchingJobs(Speciality speciality, List<Job> matching_jobs, List<Job> no_matching_jobs) {
        this.id = speciality.getId();
        this.name = speciality.getName();
        this.description = speciality.getDescription();
        this.matching_jobs = matching_jobs;
        this.no_matching_jobs = no_matching_jobs;
    }

    public void setSpeciality(Speciality speciality) {
        this.speciality = speciality;
    }

    public List<Job> getNo_matching_jobs() {
        return no_matching_jobs;
    }

    public void addMatchingJobs(Job job) {
        this.matching_jobs.add(job);
    }

    public void addNoMatchingJobs(Job job) {
        this.no_matching_jobs.add(job);
    }

    public List<Job> getMatching_jobs() {
        return matching_jobs;
    }

    @JsonBackReference
    public Speciality getSpeciality() {
        return speciality;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setMatching_jobs(List<Job> matching_jobs) {
        this.matching_jobs = matching_jobs;
    }
}
