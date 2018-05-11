package com.isep.recommendator.app.service;

import com.isep.recommendator.app.handler.ResourceNotFoundException;
import com.isep.recommendator.app.model.Job;
import com.isep.recommendator.app.model.Module;
import com.isep.recommendator.app.model.Speciality;
import com.isep.recommendator.app.model.SpecialityModule;
import com.isep.recommendator.app.repository.JobRepository;
import com.isep.recommendator.app.repository.ModuleRepository;
import com.isep.recommendator.app.repository.SpecialityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SpecialityService {
    private SpecialityRepository specialityRepository;
    private ModuleRepository     moduleRepository;
    private JobRepository        jobRepository;

    @Autowired
    public SpecialityService(SpecialityRepository specialityRepository, ModuleRepository moduleRepository, JobRepository jobRepository) {
        this.specialityRepository = specialityRepository;
        this.moduleRepository = moduleRepository;
        this.jobRepository = jobRepository;
    }

    public Speciality create(Speciality speciality) {
        return specialityRepository.save(speciality);
    }

    public Speciality getSpeciality(Long specialityId) throws ResourceNotFoundException {
        Optional<Speciality> speciality = this.specialityRepository.findById(specialityId);

        if (!speciality.isPresent())
            throw new ResourceNotFoundException("Speciality with id " + specialityId + "does not exist");

        return speciality.get();
    }

    public Speciality destroy(Long specialityId) throws ResourceNotFoundException {
        Optional<Speciality> speciality = specialityRepository.findById(specialityId);

        if (!speciality.isPresent()) {
            throw new ResourceNotFoundException("Speciality with id " + specialityId + " does not exist");
        }

        for (Job job : speciality.get().getJobs()) {
            job.getSpecialities().remove(speciality.get());
        }
        specialityRepository.delete(speciality.get());

        return speciality.get();
    }

    public Speciality addModule(Long specialityId, Long moduleId, boolean isMain) throws ResourceNotFoundException {
        Optional<Module>     module     = moduleRepository.findById(moduleId);
        Optional<Speciality> speciality = specialityRepository.findById(specialityId);

        if (!module.isPresent()) {
            throw new ResourceNotFoundException("Module with id " + moduleId + " does not exist");
        }
        if (!speciality.isPresent()) {
            throw new ResourceNotFoundException("Speciality with id " + specialityId + " does not exist");
        }

        speciality.get().getSpecialityModules().add(new SpecialityModule(speciality.get(), module.get(), isMain));
        specialityRepository.save(speciality.get());

        return speciality.get();
    }

    public Speciality addJob(Long specialityId, Long jobId) throws ResourceNotFoundException {
        Optional<Job>        job        = jobRepository.findById(jobId);
        Optional<Speciality> speciality = specialityRepository.findById(specialityId);

        if (!job.isPresent()) {
            throw new ResourceNotFoundException("Job with id " + jobId + " does not exist");
        }
        if (!speciality.isPresent()) {
            throw new ResourceNotFoundException("Speciality with id " + specialityId + " does not exist");
        }

        speciality.get().getJobs().add(job.get());
        job.get().getSpecialities().add(speciality.get());
        specialityRepository.save(speciality.get());

        return speciality.get();
    }

    public List<Speciality> getAll() {
        return specialityRepository.findAll();
    }

    public List<Module> getModulesBySpecialitiesIds(List<Long> specialitiesIds) throws ResourceNotFoundException {
        List<Module> modules = moduleRepository.findBySpecialitiesIds(specialitiesIds);

        if (modules.isEmpty())
            throw new ResourceNotFoundException("Specialities with these ids does not exist");

        return modules;
    }
}
