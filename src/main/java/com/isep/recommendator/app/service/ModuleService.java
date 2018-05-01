package com.isep.recommendator.app.service;

import com.isep.recommendator.app.model.Module;
import com.isep.recommendator.app.repository.ModuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ModuleService {

    private final ModuleRepository moduleRepo;

    @Autowired
    public ModuleService(ModuleRepository moduleRepo){
        this.moduleRepo = moduleRepo;
    }

    public Module get(Long id){
        Optional module = moduleRepo.findById(id);
        if (module.isPresent())
            return (Module) module.get();
        else
            return null;
    }

}
