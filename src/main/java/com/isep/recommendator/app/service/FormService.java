package com.isep.recommendator.app.service;

import com.isep.recommendator.app.custom_object.Form2Response;
import com.isep.recommendator.app.custom_object.ModuleWithMatchingConcepts;
import com.isep.recommendator.app.custom_object.SpeModuleConcept;
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


    public List<Form2Response> getForm2(List<Long> concept_ids){
        List<Form2Response> partial_resp = this.getPartialResponse(concept_ids);
        List<Long> spe_ids = this.getMatchingSpecialitiesIds(partial_resp);
        List<Form2Response> other_spes = specialityService.getRemainingSpecialities(spe_ids);
        partial_resp.addAll(other_spes);
        return partial_resp;
    }

    protected List<Form2Response> getPartialResponse(List<Long> concept_ids){
        List<SpeModuleConcept> query_responses = specialityRepo.getSpeModuleConceptByConceptIds(concept_ids);
        List<Form2Response> resp = this.formatMatchingSpecialities(query_responses);
        this.sortSpecialities(resp);
        return resp;
    }

    private List<Form2Response> formatMatchingSpecialities(List<SpeModuleConcept> to_format){
        List<Form2Response> resp = new ArrayList<>();
        Speciality last_speciality = null;
        // used to create Form2Response Objects
        Map<Long, ModuleWithMatchingConcepts> list_modulesAndconcepts = new HashMap<>();


        for (SpeModuleConcept query : to_format){

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

    private void formatModuleWithMatchingConcepts(Map<Long,ModuleWithMatchingConcepts> list, SpeModuleConcept query){
        Long id = query.getModule().getId();

        if (list.containsKey(id)) {
            list.get(id).addMatching_concept(query.getConcept());
        }
        else {
            ModuleWithMatchingConcepts elem = new ModuleWithMatchingConcepts(query.getModule(), query.getConcept());
            list.put(id, elem);
        }
    }

    private void addElementToResponse(List<Form2Response> response, Speciality speciality,
                                      Map<Long, ModuleWithMatchingConcepts> modules){
        Form2Response spe = new Form2Response(speciality, modules);
        response.add(spe);
    }

    // sort a list of SpecialityAndMatchingConcepts object, based on the number of matching concepts
    private void sortSpecialities(List<Form2Response> list){
        Collections.sort(list, (first, second) -> {
            if (first.getMatching() > second.getMatching())
                return -1;
            else if (first.getMatching() == second.getMatching())
                return 0;
            else
                return 1;
        });
    }

    private List<Long> getMatchingSpecialitiesIds(List<Form2Response> partial_resp){
        List<Long> spe_ids = new ArrayList<>();
        for (Form2Response obj : partial_resp){
            spe_ids.add(obj.getSpeciality().getId());
        }
        return spe_ids;
    }

}
