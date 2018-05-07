package com.isep.recommendator.app.controller;

import com.isep.recommendator.app.model.Speciality;
import com.isep.recommendator.app.repository.SpecialityRepository;
import com.isep.recommendator.app.service.SpecialityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/specialities")
@Api(value="/specialities", description="All endpoints about specialities")
public class SpecialityController {
    private SpecialityRepository specialityRepository;
    private SpecialityService specialityService;

    @Autowired
    public SpecialityController(SpecialityRepository specialityRepository, SpecialityService specialityService) {
        this.specialityRepository = specialityRepository;
        this.specialityService = specialityService;
    }

    @GetMapping("")
    @ApiOperation(value = "Get all specialities [PUBLIC]", response = Speciality.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK")
    })
    @ResponseStatus(HttpStatus.OK)
    public List<Speciality> getAll() {
        return specialityRepository.findAll();
    }
}
