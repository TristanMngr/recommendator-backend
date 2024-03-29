package com.isep.recommendator.app.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.isep.recommendator.app.custom_object.Form1Response;
import com.isep.recommendator.app.custom_object.Form2Response;
import com.isep.recommendator.app.model.User;
import com.isep.recommendator.app.service.FormJobSpeService;
import com.isep.recommendator.app.service.FormService;
import com.isep.recommendator.app.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/forms")
@Api(value="/forms", description="Endpoints to process forms data and return results")
public class FormController {

    private FormService formService;
    @Autowired
    private FormJobSpeService formJobSpeService;
    @Autowired
    private UserService userService;

    @Autowired
    public FormController(FormService formService){
        this.formService = formService;
    }

    @GetMapping("/specialities/concepts")
    @ApiOperation( value = "Get a list of specialities from a list of concept ids, and its matching concepts [USER]",
            notes="should be a connected user. \n WIP",
            response=Form2Response.class,
            responseContainer = "List")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('USER')")
    public List<Form2Response> getSpecialitiesFromConcepts(@RequestParam("concept_ids") List<Long> concept_ids, Principal principal) throws JsonProcessingException {
        List<Form2Response> resp = formService.getForm2(concept_ids);
        User currentUser = userService.getCurrentUser(principal);
        formService.saveHistory(currentUser, resp);
        return resp;
    }

    @GetMapping("/specialities/jobs")
    @ApiOperation( value = "Get a list of specialities with matching jobs [USER]",
            notes="should be a connected user. \n WIP",
            response=Form1Response.class,
            responseContainer = "List")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('USER')")
    public List getSpecialitiesFromJobs(@RequestParam("job_ids") List<Long> job_ids){
        return formJobSpeService.getForm1Response(job_ids);
    }
}
