package com.isep.recommendator.app.service;

import com.isep.recommendator.app.repository.SpecialityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SpecialityService {

    @Autowired
    private SpecialityRepository specialityRepo;

}
