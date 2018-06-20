package com.isep.recommendator.app.service;

import com.isep.recommendator.app.Application;
import com.isep.recommendator.app.custom_object.Form2Response;
import com.isep.recommendator.app.custom_object.SpeModuleConcept;
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
import java.util.List;

import static org.springframework.test.util.AssertionErrors.assertTrue;

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
    @Autowired
    FormService formService;
    @Autowired
    SpecialityModuleRepository specialityModuleRepository;

    private Concept concept_one;
    private Concept concept_two;
    private Concept concept_three;

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
        specialityModuleRepository.deleteAllInBatch();
        moduleRepository.deleteAllInBatch();
        conceptRepository.deleteAllInBatch();
        specialityRepository.deleteAllInBatch();
    }

    private void initDB(){
        concept_one = conceptRepository.save(new Concept("concept numero 1"));
        concept_two = conceptRepository.save(new Concept("concept numero 2"));
        concept_three = conceptRepository.save(new Concept("concept numero 3"));

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

        module_two = moduleService.addConcept(module_two, concept_three);
        concept_three = conceptService.get(concept_three.getId());

        spe_one = specialityService.addModule(spe_one.getId(), module_one.getId(), false);
        spe_two = specialityService.addModule(spe_two.getId(), module_two.getId(), false);
    }

    @Test
    public void repositoryRequest(){
        ArrayList<Long> concept_ids = new ArrayList<>();
        concept_ids.add(concept_one.getId());
        concept_ids.add(concept_two.getId());

        List<SpeModuleConcept> resp = specialityRepository.getSpeModuleConceptByConceptIds(concept_ids);

        assertTrue("should contains 3 elements", resp.size() == 3);
        // check the order of the list
        assertTrue("first et second element should have the same spe", resp.get(0).getSpeciality() == resp.get(1).getSpeciality());
        assertTrue("first element should be spe1", resp.get(0).getSpeciality().getId() == spe_one.getId());
        assertTrue("third element should be spe2", resp.get(2).getSpeciality().getId() == spe_two.getId());

        assertTrue("every element with the same spe should have different concepts", resp.get(0).getConcept() != resp.get(1).getConcept());
    }

    @Test
    public void getPartialResponse(){
        ArrayList<Long> concept_ids = new ArrayList<>();
        concept_ids.add(concept_one.getId());
        concept_ids.add(concept_two.getId());

        List<Form2Response> list = formService.getPartialResponse(concept_ids);

        assertTrue("it should return 2 spe", list.size() == 2);

        assertTrue("the first spe should be spe1", list.get(0).getSpeciality().getId() == spe_one.getId());
        assertTrue("it should have 1 modules", list.get(0).getMatching_modules().size() == 1);

        assertTrue("the module should have 2 concepts", list.get(0).getMatching_modules().get(0).getMatching_concepts().size() == 2);

        assertTrue("first concept should be concept one or two",
                list.get(0).getMatching_modules().get(0).getMatching_concepts().get(0).getId() == concept_one.getId() ||
                        list.get(0).getMatching_modules().get(0).getMatching_concepts().get(0).getId() ==concept_two.getId()
        );

        assertTrue("matching should be > 100", list.get(0).getMatching() > 0);
        
        assertTrue("second concept should be concept one or two and different from first",
                ( list.get(0).getMatching_modules().get(0).getMatching_concepts().get(1).getId() == concept_one.getId() ||
                        list.get(0).getMatching_modules().get(0).getMatching_concepts().get(1).getId() == concept_two.getId() ) &&
                        list.get(0).getMatching_modules().get(0).getMatching_concepts().get(1).getId() != list.get(0).getMatching_modules().get(0).getMatching_concepts().get(0).getId()
        );


        System.out.println(list.get(1).getMatching());
        assertTrue("matching should be 50", list.get(1).getMatching() == 50);

        assertTrue("the second spe should be spe2", list.get(1).getSpeciality().getId() == spe_two.getId());

        assertTrue("it should have 1 matching concepts",
                list.get(1).getMatching_modules().get(0).getMatching_concepts().size() == 1 && !list.get(1).getMatching_modules().get(0).getMatching_concepts().isEmpty());

        assertTrue("it should be concept2", list.get(1).getMatching_modules().get(0).getMatching_concepts().get(0).getId() == concept_two.getId());

    }

    @Test
    public void getForm2(){
        ArrayList<Long> concept_ids = new ArrayList<>();
        concept_ids.add(concept_one.getId());
        concept_ids.add(concept_two.getId());

        List<Form2Response> list = formService.getForm2(concept_ids);

        assertTrue("it should return 3 spe", list.size() == 3);
        assertTrue("last spe should be spe 3", list.get(2).getSpeciality().getId() == spe_three.getId());
        assertTrue("it shouldn't contain any matching modules", list.get(2).getMatching_modules().size() == 0);
        assertTrue("last spe matching should be 0", list.get(2).getMatching() == 0);

        assertTrue("all three specialities should be differents",
                list.get(0).getSpeciality().getId() != list.get(1).getSpeciality().getId() &&
                        list.get(1).getSpeciality().getId() != list.get(2).getSpeciality().getId() &&
                        list.get(2).getSpeciality().getId() != list.get(0).getSpeciality().getId()
        );
    }

    @Test
    public void getForm2_empty(){
        ArrayList<Long> concept_ids = new ArrayList<>();
        concept_ids.add(420000L);

        List<Form2Response> list = formService.getForm2(concept_ids);

        assertTrue("it should return 3 spe", list.size() == 3);

        assertTrue("all three specialities should be differents",
                list.get(0).getSpeciality().getId() != list.get(1).getSpeciality().getId() &&
                        list.get(1).getSpeciality().getId() != list.get(2).getSpeciality().getId() &&
                        list.get(2).getSpeciality().getId() != list.get(0).getSpeciality().getId()
        );
        assertTrue("spe 1 shouldn't contain any matching modules", list.get(0).getMatching_modules().size() == 0);
        assertTrue("spe 2 shouldn't contain any matching modules", list.get(1).getMatching_modules().size() == 0);
        assertTrue("spe 3 shouldn't contain any matching modules", list.get(2).getMatching_modules().size() == 0);
    }

    // TODO add test for calculatematching <3
}