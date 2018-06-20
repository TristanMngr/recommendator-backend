package com.isep.recommendator.app.service;

import com.isep.recommendator.app.Application;
import com.isep.recommendator.app.custom_object.ModuleWithMatchingConcepts;
import com.isep.recommendator.app.model.Concept;
import com.isep.recommendator.app.model.Module;
import com.isep.recommendator.app.model.Speciality;
import com.isep.recommendator.app.repository.ConceptRepository;
import com.isep.recommendator.app.repository.ModuleRepository;
import com.isep.recommendator.app.repository.SpecialityModuleRepository;
import com.isep.recommendator.app.repository.SpecialityRepository;
import com.isep.recommendator.security.config.WebSecurityConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.test.util.AssertionErrors.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class, WebSecurityConfig.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class SpecialityServiceTest {

    @Autowired
    ConceptRepository conceptRepository;

    @Autowired
    ModuleRepository moduleRepository;

    @Autowired
    SpecialityRepository specialityRepository;

    @Autowired
    SpecialityModuleRepository specialityModuleRepository;

    @Autowired
    ModuleService moduleService;

    @Autowired
    SpecialityService specialityService;

    @Before
    public void before() {
        this.clean();
    }

    private void clean() {
        specialityModuleRepository.deleteAllInBatch();
        conceptRepository.deleteAllInBatch();
        moduleRepository.deleteAllInBatch();
        specialityRepository.deleteAllInBatch();
    }


    @Test
    public void should_getMaxScore() {
        Speciality speciality_one = specialityRepository.save(new Speciality("speciality1", "..."));
        Module module_one = moduleRepository.save(new Module("module1", "..."));
        Module module_two = moduleRepository.save(new Module("module2", "..."));
        Concept concept_one = conceptRepository.save(new Concept("c1"));
        Concept concept_two = conceptRepository.save(new Concept("c2"));
        Concept concept_three = conceptRepository.save(new Concept("c3"));

        module_one = moduleService.addConcept(module_one, concept_one);
        module_one = moduleService.addConcept(module_one, concept_two);

        concept_two = conceptRepository.findByName("c2");

        module_two = moduleService.addConcept(module_two, concept_two);
        module_two = moduleService.addConcept(module_two, concept_three);

        speciality_one = specialityService.addModule(speciality_one.getId(), module_one.getId(), true);
        speciality_one = specialityService.addModule(speciality_one.getId(), module_two.getId(), false);

        assertTrue("max score should be 6", specialityService.getMaxScore(speciality_one) == 6);
    }

    @Test
    public void should_getScore(){
        Speciality speciality_one = specialityRepository.save(new Speciality("speciality1", "..."));
        Module module_one = moduleRepository.save(new Module("module1", "..."));
        Module module_two = moduleRepository.save(new Module("module2", "..."));
        Concept concept_one = conceptRepository.save(new Concept("c1"));
        Concept concept_two = conceptRepository.save(new Concept("c2"));
        Concept concept_three = conceptRepository.save(new Concept("c3"));

        module_one = moduleService.addConcept(module_one, concept_one);
        module_one = moduleService.addConcept(module_one, concept_two);

        concept_two = conceptRepository.findByName("c2");

        module_two = moduleService.addConcept(module_two, concept_two);
        module_two = moduleService.addConcept(module_two, concept_three);

        concept_three = conceptRepository.findByName("c3");

        speciality_one = specialityService.addModule(speciality_one.getId(), module_one.getId(), true);
        speciality_one = specialityService.addModule(speciality_one.getId(), module_two.getId(), false);

        // speciality contains module 1 (MAIN) concept_one et concept_two
        // AND module 2 (not main) concept_two et concept_three

        // il a choisi le concept_one
        List<Concept> module_one_matching_concepts = new ArrayList<>();
        module_one_matching_concepts.add(concept_one);

        Map<Long, ModuleWithMatchingConcepts> modules = new HashMap<>();
        modules.put(module_one.getId(), new ModuleWithMatchingConcepts(module_one, module_one_matching_concepts));
        modules.put(module_two.getId(), new ModuleWithMatchingConcepts(module_two, new ArrayList<>()));

        Double score = specialityService.getScore(speciality_one, modules);
        assertTrue("score should be 2", score == 2);

        // il a choisi aucun concept
        modules = new HashMap<>();
        modules.put(module_one.getId(), new ModuleWithMatchingConcepts(module_one, new ArrayList<>()));
        modules.put(module_two.getId(), new ModuleWithMatchingConcepts(module_two, new ArrayList<>()));

        score = specialityService.getScore(speciality_one, modules);
        assertTrue("score should be 0", score == 0);

        // il a choisi le concept_two
        module_one_matching_concepts = new ArrayList<>();
        module_one_matching_concepts.add(concept_two);
        List<Concept> module_two_matching_concepts = new ArrayList<>();
        module_two_matching_concepts.add(concept_two);

        modules = new HashMap<>();
        modules.put(module_one.getId(), new ModuleWithMatchingConcepts(module_one, module_one_matching_concepts));
        modules.put(module_two.getId(), new ModuleWithMatchingConcepts(module_two, module_two_matching_concepts));
        score = specialityService.getScore(speciality_one, modules);
        assertTrue("score should be 3", score == 3);

        // il a choisi le concept_three
        module_two_matching_concepts = new ArrayList<>();
        module_two_matching_concepts.add(concept_three);

        modules = new HashMap<>();
        modules.put(module_one.getId(), new ModuleWithMatchingConcepts(module_one, new ArrayList<>()));
        modules.put(module_two.getId(), new ModuleWithMatchingConcepts(module_two, module_two_matching_concepts));

        score = specialityService.getScore(speciality_one, modules);
        assertTrue("score should be 1", score == 1);

    }

}
