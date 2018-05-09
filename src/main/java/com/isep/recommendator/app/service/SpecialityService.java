package com.isep.recommendator.app.service;

import com.isep.recommendator.app.model.Concept;
import com.isep.recommendator.app.model.Speciality;
import com.isep.recommendator.app.custom_object.SpecialityAndMatchingConceptsList;
import com.isep.recommendator.app.custom_object.SpecialityAndConcept;
import com.isep.recommendator.app.repository.ConceptRepository;
import com.isep.recommendator.app.repository.ModuleRepository;
import com.isep.recommendator.app.repository.SpecialityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SpecialityService {

    @Autowired
    private SpecialityRepository specialityRepo;

    @Autowired
    private ModuleRepository moduleRepo;

    @Autowired
    private ConceptRepository conceptRepo;

    public SpecialityAndMatchingConceptsList buildSpecialityConceptObject(Speciality speciality, List<Long> concept_ids){
        List<Concept> concept_list = moduleRepo.getConceptBySpeIdAndConceptsIds(speciality.getId(), concept_ids);
        return new SpecialityAndMatchingConceptsList(speciality, concept_list);
    }

    public List<SpecialityAndMatchingConceptsList> getSpecialitiesByConceptsIdsWithMatching(List<Long> concept_ids){
        List<SpecialityAndMatchingConceptsList> resp = new ArrayList<>();
        List<Concept> list_concepts = new ArrayList<>();
        Speciality comparator = null;
        List<SpecialityAndConcept> query_responses = moduleRepo.getSpecialityAndConceptByConceptIds(concept_ids);

        for (SpecialityAndConcept query : query_responses){
            //Long id = query.getSpeciality().getId();
            list_concepts.add(query.getConcept());

            // si c'est une nouvelle spé, on sauvegarde la derniere et on vide la liste des concepts
            if (query.getSpeciality() != comparator){
                resp.add(new SpecialityAndMatchingConceptsList(query.getSpeciality(), list_concepts));
                list_concepts = new ArrayList<>();
            }
            // si on a pas encore sauvegardé la derniere spé, et qu'il s'agit là du dernier element de la liste
            else if (query.getSpeciality() == comparator && query_responses.indexOf(query) == query_responses.size() -1) {
                resp.add(new SpecialityAndMatchingConceptsList(query.getSpeciality(), list_concepts));
            }

            comparator = query.getSpeciality();
        }
        return resp;
    }

//    public List<SpecialityAndMatchingConceptsList> getSpecialitiesByConceptsIdsWithMatching(List<Long> concept_ids){
//        List<SpecialityAndMatchingConceptsList> resp = new ArrayList<>();
//        List<Speciality> specialities = moduleRepo.getSpecialitiesByConceptsIds(concept_ids);
//        for (Speciality spe : specialities){
//            SpecialityAndMatchingConceptsList object = this.buildSpecialityConceptObject(spe, concept_ids);
//            resp.add(object);
//        }
//        return resp;
//    }

}
