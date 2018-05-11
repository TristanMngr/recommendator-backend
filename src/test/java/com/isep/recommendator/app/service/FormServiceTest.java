package com.isep.recommendator.app.service;

import com.isep.recommendator.app.Application;
import com.isep.recommendator.app.custom_object.SpecialityAndMatchingConceptsObject;
import com.isep.recommendator.app.model.Concept;
import com.isep.recommendator.app.model.Module;
import com.isep.recommendator.app.model.Speciality;
import com.isep.recommendator.app.repository.ConceptRepository;
import com.isep.recommendator.app.repository.ModuleRepository;
import com.isep.recommendator.app.repository.SpecialityRepository;
import com.isep.recommendator.security.config.WebSecurityConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.util.AssertionErrors.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class, WebSecurityConfig.class})
public class FormServiceTest {

    @Autowired
    ConceptService conceptService;
    @Autowired
    ConceptRepository conceptRepository;
    @Autowired
    ModuleService moduleService;
    @Autowired
    ModuleRepository moduleRepository;
    @Autowired
    SpecialityRepository specialityRepository;

    @Before
    public void before(){
        moduleRepository.deleteAllInBatch();
        conceptRepository.deleteAllInBatch();
        specialityRepository.deleteAllInBatch();
    }

    //@Test
    //public void getSpecialitiesByConceptsIdsWithMatching(){
//        ArrayList<Long> concept_ids = new ArrayList<>();
//        concept_ids.add(concept1.getId());
//        concept_ids.add(concept4.getId());
//        this.makeLinks();
//        List<SpecialityAndMatchingConceptsObject> list = formService.getSpecialitiesByConceptsIdsWithMatching(concept_ids);
//        System.out.println(list);
//        assertTrue("it should return 3 spe", list.size() == 3);
//        assertTrue("", list.get(0).getMatching_concepts().size() == 2);
    //}

    @Test
    public void getSpecialitiesAndMatchingConceptByConceptsIds(){
        Concept concept_one = conceptRepository.save(new Concept("concept numero 1"));
        Concept concept_two = conceptRepository.save(new Concept("concept numero 2"));

        Module module_one = moduleRepository.save(new Module("module 1", "ceci est le module 1"));
        Module module_two = moduleRepository.save(new Module("module 2", "ceci est le module 2"));

        module_one = moduleService.addConcept(module_one, concept_one);
        concept_one = conceptService.get(concept_two.getId());

        module_one = moduleService.addConcept(module_one, concept_two);
        concept_two = conceptService.get(concept_two.getId());

        module_two = moduleService.addConcept(module_two, concept_two);
        concept_two = conceptService.get(concept_two.getId());

        Speciality spe_one = specialityRepository.save(new Speciality("speciality 1", "ceci est la spe 1"));
        Speciality spe_two = specialityRepository.save(new Speciality("speciality 2", "ceci est la spe 2"));
        Speciality spe_three = specialityRepository.save(new Speciality("speciality 3", "ceci est la spe 3"));




        // verif ordonné par chiffres
    }

}