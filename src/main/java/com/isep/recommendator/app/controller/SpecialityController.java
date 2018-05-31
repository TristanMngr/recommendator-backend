package com.isep.recommendator.app.controller;

import com.isep.recommendator.app.handler.BadRequestException;
import com.isep.recommendator.app.model.Module;
import com.isep.recommendator.app.model.Speciality;
import com.isep.recommendator.app.repository.ModuleRepository;
import com.isep.recommendator.app.service.SpecialityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@RequestMapping("/specialities")
@Api(value = "/specialities", description = "All endpoints about specialities")
public class SpecialityController {
    private SpecialityService specialityService;

    @Autowired
    public SpecialityController(SpecialityService specialityService, ModuleRepository moduleRepository) {
        this.specialityService = specialityService;
    }


    @GetMapping("")
    @ApiOperation(value = "Get all specialities [USER]", response = Speciality.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK")
    })
    @ResponseStatus(HttpStatus.OK)
    public List<Speciality> getAll() {
        return this.specialityService.getAll();
    }


    @PostMapping("")
    @ApiOperation(value = "create Speciality [ADMIN]", response = Speciality.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created")
    })
    @PreAuthorize("hasAuthority('ADMIN')")
    public Speciality create(@RequestParam(value = "name") String name, @RequestParam(value = "description", required = false) String description) throws BadRequestException {
        return specialityService.create(name, description);
    }


    @GetMapping("/{speciality_id}")
    @ApiOperation(value = "Get Speciality", response = Speciality.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK")
    })
    public Speciality getSpeciality(@PathVariable(value = "speciality_id") Long specialityId) {
        return specialityService.getSpeciality(specialityId);
    }


    @DeleteMapping("/{speciality_id}")
    @ApiOperation(value = "Delete Speciality [ADMIN]", response = Speciality.class)
    @PreAuthorize("hasAuthority('ADMIN')")
    public Speciality destroy(@PathVariable("speciality_id") Long specialityId) {
        return this.specialityService.destroy(specialityId);
    }


    @ApiOperation(value = "Get all modules from listed specialitiesIds [USER]", response = Speciality.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok")
    })
    @GetMapping("/{specialitiesIds}/modules")
    @ResponseStatus(HttpStatus.OK)
    public List<Module> getModules(@PathVariable(value = "specialitiesIds") List<Long> specialitiesIds) {
        return specialityService.getModulesBySpecialitiesIds(specialitiesIds);
    }


    @ApiOperation(value = "Associate Modules with a speciality [ADMIN]", response = Speciality.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created")
    })
    @PostMapping("/{speciality_id}/modules")
    public Speciality addModule(@PathVariable("speciality_id") Long specialityId, @RequestParam("module_id") Long moduleId, @RequestParam("is_main") Boolean isMain) {
        return specialityService.addModule(specialityId, moduleId, isMain);
    }


    @ApiOperation(value = "Associate a job with a speciality [ADMIN]", response = Speciality.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created")
    })
    @PostMapping("/{speciality_id}/jobs")
    public Speciality addJob(@PathVariable("speciality_id") Long specialityId, @RequestParam("job_id") Long jobId) {
        return specialityService.addJob(specialityId, jobId);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ApiOperation(value = "Update a concept [ADMIN]", notes="should be admin", response = Speciality.class)
    public Speciality updateById(@PathVariable(value = "id") Long id, @RequestParam(value = "name", required = false) String name, @RequestParam(value = "description", required = false) String description) throws BadRequestException {
        Speciality spe = specialityService.getSpeciality(id);
        String new_name = name == null ? spe.getName() : name;
        String new_description = description == null ? spe.getDescription() : description;
        spe = specialityService.update(spe, new_name, new_description);
        return spe;
    }
}
