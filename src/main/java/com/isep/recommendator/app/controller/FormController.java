package com.isep.recommendator.app.controller;

import com.isep.recommendator.app.custom_object.SpecialityAndMatchingConceptsObject;
import com.isep.recommendator.app.service.FormService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/forms")
@Api(value="/forms", description="Endpoints to process forms data and return results")
public class FormController {

    private FormService formService;

    @Autowired
    public FormController(FormService formService){
        this.formService = formService;
    }

    @GetMapping("/specialities/concepts")
    @ApiOperation( value = "Get a list of specialities from a list of concept ids, and its matching concepts [USER]",
            notes="should be a connected user. \n Retourne une liste d'objets qui contiennent chacun une spé et les concepts qui sont à la fois dans la requete, et la spé",
            response=SpecialityAndMatchingConceptsObject.class,
            responseContainer = "List")
    @ResponseStatus(HttpStatus.OK)
    //@PreAuthorize("hasAuthority('USER')")
    public List<SpecialityAndMatchingConceptsObject> getSpecialitiesFromConcepts(@RequestParam("concept_ids") List<Long> concept_ids){
        return formService.getAllSpecialitiesWithMatching(concept_ids);
    }
}
