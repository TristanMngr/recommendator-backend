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

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.util.AssertionErrors.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class, WebSecurityConfig.class})
public class FormServiceTest {

    @Test
    public void getSpecialitiesByConceptsIdsWithMatching(){
//        ArrayList<Long> concept_ids = new ArrayList<>();
//        concept_ids.add(concept1.getId());
//        concept_ids.add(concept4.getId());
//        this.makeLinks();
//        List<SpecialityAndMatchingConceptsObject> list = formService.getSpecialitiesByConceptsIdsWithMatching(concept_ids);
//        System.out.println(list);
//        assertTrue("it should return 3 spe", list.size() == 3);
//        assertTrue("", list.get(0).getMatching_concepts().size() == 2);
    }

}