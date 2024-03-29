package com.isep.recommendator.app.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.isep.recommendator.app.custom_object.Form2Response;
import com.isep.recommendator.app.custom_object.ModuleWithMatchingConcepts;
import com.isep.recommendator.app.custom_object.SpeModuleConcept;
import com.isep.recommendator.app.model.History;
import com.isep.recommendator.app.model.Speciality;
import com.isep.recommendator.app.model.User;
import com.isep.recommendator.app.repository.HistoryRepository;
import com.isep.recommendator.app.repository.SpecialityRepository;
import com.isep.recommendator.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FormService extends Utils<Form2Response> {

    @Autowired
    SpecialityRepository specialityRepo;
    @Autowired
    SpecialityService specialityService;
    @Autowired
    HistoryRepository historyRepository;
    @Autowired
    UserRepository userRepository;


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
        sortWithAttributes(resp);
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

        int matching = this.calculateMatchingForm2(speciality, modules);
        Form2Response spe = new Form2Response(speciality, modules, matching);
        response.add(spe);
    }

    private int calculateMatchingForm2(Speciality speciality, Map<Long, ModuleWithMatchingConcepts> modules){
        Double score = specialityService.getScore(speciality, modules);
        Double matching = (score / specialityService.getMaxScore(speciality)) * 100;
        return matching.intValue();
    }

    private List<Long> getMatchingSpecialitiesIds(List<Form2Response> partial_resp){
        List<Long> spe_ids = new ArrayList<>();
        for (Form2Response obj : partial_resp){
            spe_ids.add(obj.getSpeciality().getId());
        }
        return spe_ids;
    }

    public void saveHistory(User user, List<Form2Response> resp) throws JsonProcessingException {
        String name = "Historique du " + new Date();
        String json = this.listToJson(resp);
        History history = new History(name, "form2", json, user);
        historyRepository.save(history);
        user.addHistory(history);
        userRepository.save(user);
    }

    private String listToJson(List<?> list) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(list);
    }
}
