package com.isep.recommendator.app.service;

import com.isep.recommendator.app.custom_object.SpecialityAndConceptObject;
import com.isep.recommendator.app.custom_object.SpecialityAndMatchingConceptsObject;
import com.isep.recommendator.app.model.Concept;
import com.isep.recommendator.app.model.Speciality;
import com.isep.recommendator.app.repository.ModuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class FormService {

    @Autowired
    ModuleRepository moduleRepo;

    // V1 du formulaire de la release 2. WORK IN PROGRESS !
    public List<SpecialityAndMatchingConceptsObject> getSpecialitiesByConceptsIdsWithMatching(List<Long> concept_ids){
        List<SpecialityAndMatchingConceptsObject> resp = new ArrayList<>();

        List<SpecialityAndConceptObject> query_responses = moduleRepo.getSpecialitiesAndMatchingConceptByConceptsIds(concept_ids);
        //  used to know the spe of the last iteration
        Speciality last_speciality = null;
        // used to create SpecialityAndMatchingConcepts Objects
        List<Concept> list_concepts = new ArrayList<>();

        for (SpecialityAndConceptObject query : query_responses){

            if (last_speciality != null && query.getSpeciality() != last_speciality){
                this.addSpecialityAndMatchingConceptsToResponse(resp, last_speciality, list_concepts);
                list_concepts = new ArrayList<>();
            }

            list_concepts.add(query.getConcept());

            // si c'est la derniere itération
            if (query_responses.indexOf(query) == query_responses.size()-1){
                this.addSpecialityAndMatchingConceptsToResponse(resp, query.getSpeciality(), list_concepts);
            }

            last_speciality = query.getSpeciality();
        }

        this.sortSpecialities(resp);
        return resp;

    }

    // add an object "SpecialityAndMatchingConceptsObject" to a list, and return this list
    private List<SpecialityAndMatchingConceptsObject> addSpecialityAndMatchingConceptsToResponse(List<SpecialityAndMatchingConceptsObject> response,
                                                       Speciality speciality,
                                                       List<Concept> list_concepts){
        SpecialityAndMatchingConceptsObject spe = new SpecialityAndMatchingConceptsObject(speciality, list_concepts);
        response.add(spe);
        return response;
    }

    // sort a list of SpecialityAndMatchingConcepts object, based on the number of matching concepts
    private void sortSpecialities(List<SpecialityAndMatchingConceptsObject> list){
        Collections.sort(list, (first, second) -> {
            if (first.getMatching_concepts().size() > second.getMatching_concepts().size())
                return -1;
            else if (first.getMatching_concepts().size() < second.getMatching_concepts().size())
                return 1;
            else
                return 0;
        });
    }

}
