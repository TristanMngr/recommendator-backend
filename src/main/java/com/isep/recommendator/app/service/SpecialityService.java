package com.isep.recommendator.app.service;

import com.isep.recommendator.app.model.Concept;
import com.isep.recommendator.app.model.Speciality;
import com.isep.recommendator.app.model.SpecialityConceptFormResponse;
import com.isep.recommendator.app.model.SpecialityConceptQueryResponse;
import com.isep.recommendator.app.repository.ConceptRepository;
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

    @Autowired
    private ConceptRepository conceptRepo;

    public SpecialityConceptFormResponse buildSpecialityConceptObject(Speciality speciality, List<Long> concept_ids){
        List<Concept> concept_list = moduleRepo.getConceptBySpeIdAndConceptsIds(speciality.getId(), concept_ids);
        return new SpecialityConceptFormResponse(speciality, concept_list);
    }

    public HashMap<Long, SpecialityConceptFormResponse> getSpecialitiesByConceptsIdsWithMatching(List<Long> concept_ids){
        HashMap<Long, SpecialityConceptFormResponse> resp = new HashMap<>();
        List<Concept> list_concepts = new ArrayList<>();
        Speciality comparator = null;

        List<SpecialityConceptQueryResponse> query_responses = moduleRepo.getSpecialityAndConceptByConceptIds(concept_ids);
        for (SpecialityConceptQueryResponse query : query_responses){
            Long id = query.getSpeciality().getId();

            // si c'est une nouvelle spé, on vide la liste des concepts
            if (query.getSpeciality() != comparator){
                list_concepts = new ArrayList<>();
            }

            list_concepts.add(query.getConcept());
            resp.put(id, new SpecialityConceptFormResponse(query.getSpeciality(), list_concepts));
            comparator = query.getSpeciality();

        }
        return resp;
    }

//    public List<SpecialityConceptFormResponse> getSpecialitiesByConceptsIdsWithMatching(List<Long> concept_ids){
//        List<SpecialityConceptFormResponse> resp = new ArrayList<>();
//        List<Speciality> specialities = moduleRepo.getSpecialitiesByConceptsIds(concept_ids);
//        for (Speciality spe : specialities){
//            SpecialityConceptFormResponse object = this.buildSpecialityConceptObject(spe, concept_ids);
//            resp.add(object);
//        }
//        return resp;
//    }

}
