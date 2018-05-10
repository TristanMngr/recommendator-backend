package com.isep.recommendator.app.service;

import com.isep.recommendator.app.custom_object.SpecialityAndConceptObject;
import com.isep.recommendator.app.custom_object.SpecialityAndMatchingConceptsObject;
import com.isep.recommendator.app.model.Concept;
import com.isep.recommendator.app.model.Speciality;
import com.isep.recommendator.app.repository.ModuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FormService {

    @Autowired
    ModuleRepository moduleRepo;

    // V1 du formulaire de la release 2 (avoir des spé a partir d'une liste de concepts)
    public List<SpecialityAndMatchingConceptsObject> getSpecialitiesByConceptsIdsWithMatching(List<Long> concept_ids){
        List<SpecialityAndMatchingConceptsObject> resp = new ArrayList<>();

        List<SpecialityAndConceptObject> query_responses = moduleRepo.getSpecialitiesAndMatchingConceptByConceptsIds(concept_ids);

        //  used to know the spe of the last iteration
        Speciality comparator = null;

        // used to create SpecialityAndMatchingConcepts Objects
        List<Concept> list_concepts = new ArrayList<>();

        for (SpecialityAndConceptObject query : query_responses){
            list_concepts.add(query.getConcept());

            // si ce n'est pas la même spé que la derniere itération
            if (query.getSpeciality() != comparator){
                this.addSpecialityAndMatchingConceptsToResponse(resp, query.getSpeciality(), list_concepts);
                // on vide la list de matching concepts pour les prochaines itérations
                list_concepts = new ArrayList<>();
            }
            // si on n'a pas encore ajouté la spé a la réponse et qu'il s'agit là de la derniere itération
            else if (query.getSpeciality() == comparator && query_responses.indexOf(query) == query_responses.size() -1) {
                this.addSpecialityAndMatchingConceptsToResponse(resp, query.getSpeciality(), list_concepts);
            }

            comparator = query.getSpeciality();
        }
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

}
