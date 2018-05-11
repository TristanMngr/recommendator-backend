package com.isep.recommendator.app.service;

import com.isep.recommendator.app.handler.ResourceNotFoundException;
import com.isep.recommendator.app.model.Module;
import com.isep.recommendator.app.model.Speciality;
import com.isep.recommendator.app.model.SpecialityModule;
import com.isep.recommendator.app.repository.ModuleRepository;
import com.isep.recommendator.app.repository.SpecialityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SpecialityService {

    @Autowired
    private SpecialityRepository specialityRepository;
    @Autowired
    private ModuleRepository moduleRepository;

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

}
