package com.isep.recommendator.app.service;

import com.isep.recommendator.app.Application;
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

import static org.springframework.test.util.AssertionErrors.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class, WebSecurityConfig.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class ModuleServiceTest {
    @Autowired
    ModuleService moduleService;

    @Autowired
    SpecialityService specialityService;

    @Autowired
    SpecialityModuleRepository specialityModuleRepository;

    @Autowired
    ConceptRepository conceptRepository;

    @Autowired
    ModuleRepository moduleRepository;

    @Autowired
    SpecialityRepository specialityRepository;

    private Module     moduleOne;
    private Speciality specialityOne;
    private Speciality specialityTwo;

    @Before
    public void before() {
        this.clean();
    }

    private void clean() {
        moduleRepository.deleteAllInBatch();
        specialityRepository.deleteAllInBatch();
    }


    @Test
    public void shouldDeleteWhenNoLink() {
        moduleOne = moduleRepository.save(new Module("moduleOne", "moduleOne"));
        moduleService.delete(moduleOne);

        assertTrue("it should not return module", moduleService.getAll().size() == 0);
    }

    @Test
    public void shouldDeleteWhenLinkWithSpeciality() {
        moduleOne = moduleRepository.save(new Module("moduleOne", "moduleOne"));
        specialityOne = specialityRepository.save(new Speciality("specialityOne", "specialityOne"));

        specialityService.addModule(specialityOne.getId(), moduleOne.getId(), true);

        moduleService.delete(moduleOne);

        assertTrue("it should not return module", moduleService.getAll().size() == 0);
    }

    @Test
    public void shouldDeleteWhenLinkWithSpecialities() {
        moduleOne = moduleRepository.save(new Module("moduleOne", "moduleOne"));
        specialityOne = specialityRepository.save(new Speciality("specialityOne", "specialityOne"));
        specialityTwo = specialityRepository.save(new Speciality("specialityTwo", "specialityTwo"));

        specialityService.addModule(specialityOne.getId(), moduleOne.getId(), true);
        specialityService.addModule(specialityTwo.getId(), moduleOne.getId(), false);

        moduleService.delete(moduleOne);

        assertTrue("it should not return module", moduleService.getAll().size() == 0);
    }

}
