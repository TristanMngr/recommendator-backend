package com.isep.recommendator.app.service;

import com.isep.recommendator.app.Application;
import com.isep.recommendator.app.custom_object.SpecialityAndConceptObject;
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
    @Autowired
    SpecialityService specialityService;

    private Concept concept_one;
    private Concept concept_two;

    private Module module_one;
    private Module module_two;

    private Speciality spe_one;
    private Speciality spe_two;
    private Speciality spe_three;

    @Before
    public void before(){
        this.clean();
        this.initDB();
        this.makeLinks();
    }

    private void clean(){
        moduleRepository.deleteAllInBatch();
        conceptRepository.deleteAllInBatch();
        specialityRepository.deleteAllInBatch();
    }

    private void initDB(){
        concept_one = conceptRepository.save(new Concept("concept numero 1"));
        concept_two = conceptRepository.save(new Concept("concept numero 2"));

        module_one = moduleRepository.save(new Module("module 1", "ceci est le module 1"));
        module_two = moduleRepository.save(new Module("module 2", "ceci est le module 2"));

        spe_one = specialityRepository.save(new Speciality("speciality 1", "ceci est la spe 1"));
        spe_two = specialityRepository.save(new Speciality("speciality 2", "ceci est la spe 2"));
        spe_three = specialityRepository.save(new Speciality("speciality 3", "ceci est la spe 3"));
    }

    private void makeLinks(){
        module_one = moduleService.addConcept(module_one, concept_one);
        concept_one = conceptService.get(concept_one.getId());

        module_one = moduleService.addConcept(module_one, concept_two);
        concept_two = conceptService.get(concept_two.getId());

        module_two = moduleService.addConcept(module_two, concept_two);
        concept_two = conceptService.get(concept_two.getId());
        spe_one = specialityService.addModule(spe_one.getId(), module_one.getId(), false);
        spe_two = specialityService.addModule(spe_two.getId(), module_two.getId(), false);
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
        ArrayList<Long> concept_ids = new ArrayList<>();
        concept_ids.add(concept_one.getId());
        concept_ids.add(concept_two.getId());

        List<SpecialityAndConceptObject> resp = moduleRepository.getSpecialitiesAndMatchingConceptByConceptsIds(concept_ids);

        assertTrue("should contains 3 elements", resp.size() == 3);
        // check the order of the list
        assertTrue("first et second element should have the same spe", resp.get(0).getSpeciality() == resp.get(1).getSpeciality());
        assertTrue("first element should be spe1", resp.get(0).getSpeciality().getId() == spe_one.getId());
        assertTrue("third element should be spe2", resp.get(2).getSpeciality().getId() == spe_two.getId());

        assertTrue("every element with the same spe should have different concepts", resp.get(0).getConcept() != resp.get(1).getConcept());
    }

}