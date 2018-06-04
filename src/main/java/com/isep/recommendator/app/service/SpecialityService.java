package com.isep.recommendator.app.service;

import com.isep.recommendator.app.custom_object.Form2Response;
import com.isep.recommendator.app.handler.BadRequestException;
import com.isep.recommendator.app.handler.CustomValidationException;
import com.isep.recommendator.app.handler.ResourceNotFoundException;
import com.isep.recommendator.app.model.*;
import com.isep.recommendator.app.repository.JobRepository;
import com.isep.recommendator.app.repository.ModuleRepository;
import com.isep.recommendator.app.repository.SpecialityModuleRepository;
import com.isep.recommendator.app.repository.SpecialityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Service
public class SpecialityService {
    private SpecialityRepository specialityRepository;
    private ModuleRepository     moduleRepository;
    private JobRepository        jobRepository;
    private SpecialityModuleRepository specialityModuleRepository;

    @Autowired
    public SpecialityService(SpecialityRepository specialityRepository, ModuleRepository moduleRepository, JobRepository jobRepository,
                             SpecialityModuleRepository specialityModuleRepository) {
        this.specialityRepository = specialityRepository;
        this.moduleRepository = moduleRepository;
        this.jobRepository = jobRepository;
        this.specialityModuleRepository = specialityModuleRepository;
    }

    public Speciality create(String name, String description) throws BadRequestException {
        specialityAlreadyExist(name);

        try {
            @Valid Speciality speciality = new Speciality(name, description) ;
            return specialityRepository.save(speciality);
        } catch (ConstraintViolationException e) {
            throw new CustomValidationException(e);
        }

    }

    public Speciality getSpeciality(Long specialityId) throws ResourceNotFoundException {
        return specialityFound(specialityId);

    }

    public Speciality destroy(Long specialityId) throws ResourceNotFoundException {
        Speciality speciality = specialityFound(specialityId);

        for (Job job : speciality.getJobs()) {
            job.getSpecialities().remove(speciality);
        }
        specialityRepository.delete(speciality);

        return speciality;
    }

    public Speciality addModule(Long specialityId, Long moduleId, boolean isMain) throws ResourceNotFoundException {
        Optional<Module>     module     = moduleRepository.findById(moduleId);
        Speciality speciality = specialityFound(specialityId);

        if (!module.isPresent()) {
            throw new ResourceNotFoundException("Module with id " + moduleId + " does not exist");
        }

        speciality.getSpecialityModules().add(new SpecialityModule(speciality, module.get(), isMain));
        specialityRepository.save(speciality);

        return speciality;
    }

    private SpecialityModule getSpecialityModuleLink(Long specialityId, Long moduleId) throws BadRequestException {
        SpecialityModule link = specialityModuleRepository.findBySpecialitiesIds(specialityId, moduleId);
        if (link == null)
            throw new BadRequestException("module with id "+ moduleId + " isn't in speciality with id " + specialityId);

        return link;
    }

    public Speciality removeModule(Long specialityId, Long moduleId) throws ResourceNotFoundException, BadRequestException {
        SpecialityModule link = this.getSpecialityModuleLink(specialityId, moduleId);
        Speciality speciality = specialityFound(specialityId);
        speciality.getSpecialityModules().remove(link);
        specialityModuleRepository.delete(link);

        return this.getSpeciality(specialityId);
    }

    public Speciality setIsMain(Long specialityId, Long moduleId, Boolean is_main) throws BadRequestException {
        SpecialityModule link = this.getSpecialityModuleLink(specialityId, moduleId);
        link.setMainModule(is_main);
        specialityModuleRepository.save(link);
        return this.specialityFound(specialityId);
    }

    public Speciality addJob(Long specialityId, Long jobId) throws ResourceNotFoundException {
        Optional<Job>        job        = jobRepository.findById(jobId);
        Speciality speciality = specialityFound(specialityId);

        if (!job.isPresent()) {
            throw new ResourceNotFoundException("Job with id " + jobId + " does not exist");
        }

        speciality.getJobs().add(job.get());
        job.get().getSpecialities().add(speciality);
        specialityRepository.save(speciality);

        return speciality;
    }

    public Speciality removeJob(Long specialityId, Long jobId) throws ResourceNotFoundException, BadRequestException {
        Optional<Job>        job        = jobRepository.findById(jobId);
        Speciality speciality = specialityFound(specialityId);

        if (!job.isPresent()) {
            throw new ResourceNotFoundException("Job with id " + jobId + " does not exist");
        }

        if (!speciality.getJobs().contains(job.get()))
            throw new BadRequestException("job with id "+ jobId + " isn't in speciality with id " + specialityId);

        speciality.getJobs().remove(job.get());
        job.get().getSpecialities().remove(speciality);
        specialityRepository.save(speciality);

        return speciality;
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

    // Custom message errors

    public Speciality specialityFound(Long specialityId) {
        Optional<Speciality> speciality = this.specialityRepository.findById(specialityId);

        if (!speciality.isPresent())
            throw new ResourceNotFoundException("Speciality with id " + specialityId + "does not exist");

        return speciality.get();
    }

    public void specialityAlreadyExist(String name) throws BadRequestException {
        if (specialityRepository.findByName(name) != null) {
            throw new BadRequestException("Speciality with name " + name + " already exist");
        }
    }

    public List<Form2Response> getRemainingSpecialities(List<Long> excluded_ids){
        if (excluded_ids.isEmpty())
            return specialityRepository.getAllSpecialitiesWithNoMatchingConcepts();

        return specialityRepository.getRemainingSpecialitiesAndNullMatchingConcepts(excluded_ids);
    }

    public int getMaxScore(Speciality speciality){
        int score = specialityRepository.getMaxScore(speciality.getId());
        return score;
    }

    public Speciality update(Speciality speciality, String new_name, String new_desc) throws BadRequestException{
        if (specialityRepository.findByName(new_name) != null)
            throw new BadRequestException("speciality with name " + new_name + " already exist");

        if (!speciality.getName().equals(new_name))
            speciality.setName(new_name);
        if (!speciality.getDescription().equals(new_desc))
            speciality.setDescription(new_desc);

        return specialityRepository.save(speciality);
    }
}
