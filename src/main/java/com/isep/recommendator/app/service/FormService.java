package com.isep.recommendator.app.service;

import com.isep.recommendator.app.custom_object.Form_2_Response;
import com.isep.recommendator.app.custom_object.ModuleWithMatchingConcepts;
import com.isep.recommendator.app.custom_object.Spe_Module_Concept;
import com.isep.recommendator.app.model.Speciality;
import com.isep.recommendator.app.repository.SpecialityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FormService {

    @Autowired
    SpecialityRepository specialityRepo;
    @Autowired
    SpecialityService specialityService;


    public List<Form_2_Response> getForm2(List<Long> concept_ids){
        List<Form_2_Response> partial_resp = this.getPartialResponse(concept_ids);
        List<Long> spe_ids = this.getMatchingSpecialitiesIds(partial_resp);
        List<Form_2_Response> other_spes = specialityService.getRemainingSpecialities(spe_ids);
        partial_resp.addAll(other_spes);
        return partial_resp;
    }

    protected List<Form_2_Response> getPartialResponse(List<Long> concept_ids){
        List<Spe_Module_Concept> query_responses = specialityRepo.getSpe_Module_Concept(concept_ids);
        List<Form_2_Response> resp = this.formatMatchingSpecialities(query_responses);
        this.sortSpecialities(resp);
        return resp;
    }

    private List<Form_2_Response> formatMatchingSpecialities(List<Spe_Module_Concept> to_format){
        List<Form_2_Response> resp = new ArrayList<>();
        Speciality last_speciality = null;
        // used to create Form_2_Response Objects
        Map<Long, ModuleWithMatchingConcepts> list_modulesAndconcepts = new HashMap<>();


        for (Spe_Module_Concept query : to_format){

            if (last_speciality != null && query.getSpeciality() != last_speciality){
                this.addElementToResponse(resp, last_speciality, list_modulesAndconcepts);
                list_modulesAndconcepts = new HashMap<>();
            }

            // list_concepts.add(query.getConcept());
            this.formatModuleWithMatchingConcepts(list_modulesAndconcepts, query);


            // si c'est la derniere itération
            if (to_format.indexOf(query) == to_format.size()-1){
                this.addElementToResponse(resp, query.getSpeciality(), list_modulesAndconcepts);
            }

            last_speciality = query.getSpeciality();
        }

        return resp;

    }

    private void formatModuleWithMatchingConcepts(Map<Long,ModuleWithMatchingConcepts> list, Spe_Module_Concept query){
        Long id = query.getModule().getId();

        if (list.containsKey(id)) {
            list.get(id).addMatching_concept(query.getConcept());
        }
        else {
            ModuleWithMatchingConcepts elem = new ModuleWithMatchingConcepts(query.getModule(), query.getConcept());
            list.put(id, elem);
        }
    }

    private void addElementToResponse(List<Form_2_Response> response, Speciality speciality,
                                      Map<Long, ModuleWithMatchingConcepts> modules){
        Form_2_Response spe = new Form_2_Response(speciality, modules);
        response.add(spe);
    }

    // sort a list of SpecialityAndMatchingConcepts object, based on the number of matching concepts
    private void sortSpecialities(List<Form_2_Response> list){
        Collections.sort(list, (first, second) -> {
            if (first.getMatching() > second.getMatching())
                return -1;
            else if (first.getMatching() == second.getMatching())
                return 0;
            else
                return 1;
        });
    }

    private List<Long> getMatchingSpecialitiesIds(List<Form_2_Response> partial_resp){
        List<Long> spe_ids = new ArrayList<>();
        for (Form_2_Response obj : partial_resp){
            spe_ids.add(obj.getSpeciality().getId());
        }
        return spe_ids;
    }

}
