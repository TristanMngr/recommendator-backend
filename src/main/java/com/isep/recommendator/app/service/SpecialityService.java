package com.isep.recommendator.app.service;

import com.isep.recommendator.app.model.Concept;
import com.isep.recommendator.app.model.Speciality;
import com.isep.recommendator.app.model.SpecialityConceptFormResponse;
import com.isep.recommendator.app.repository.ModuleRepository;
import com.isep.recommendator.app.repository.SpecialityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SpecialityService {

    @Autowired
    private SpecialityRepository specialityRepo;

    @Autowired
    private ModuleRepository moduleRepo;
    private Object spe;

    public List<?> getSpecialitiesByConceptsIds(List<Long> concept_ids){
        return moduleRepo.getSpecialitiesByConceptsIds(concept_ids);
    }

    public SpecialityConceptFormResponse buildSpecialityConceptObject(Speciality speciality, List<Long> concept_ids){
        List<Concept> concept_list = moduleRepo.getConceptBySpeIdAndConceptsIds(speciality.getId(), concept_ids);
        return new SpecialityConceptFormResponse(speciality, concept_list);
    }

    public List<SpecialityConceptFormResponse> getSpecialitiesByConceptsIdsWithMatching(List<Long> concept_ids){
        List<SpecialityConceptFormResponse> resp = new ArrayList<>();
        List<Speciality> specialities = moduleRepo.getSpecialitiesByConceptsIds(concept_ids);
        for (Speciality spe : specialities){
            SpecialityConceptFormResponse object = this.buildSpecialityConceptObject(spe, concept_ids);
            resp.add(object);
        }
        return resp;
    }

}
