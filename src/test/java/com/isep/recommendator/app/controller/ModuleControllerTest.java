package com.isep.recommendator.app.controller;

import com.isep.recommendator.app.Application;
import com.isep.recommendator.app.model.Concept;
import com.isep.recommendator.app.model.Module;
import com.isep.recommendator.app.repository.ConceptRepository;
import com.isep.recommendator.app.repository.ModuleRepository;
import com.isep.recommendator.app.service.ConceptService;
import com.isep.recommendator.app.service.ModuleService;
import com.isep.recommendator.security.config.WebSecurityConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.Charset;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertFalse;
import static org.springframework.test.util.AssertionErrors.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class, WebSecurityConfig.class})
@WebAppConfiguration
public class ModuleControllerTest {

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;

    @Autowired
    private ModuleRepository moduleRepo;

    @Autowired
    private ModuleService moduleService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ConceptRepository conceptRepo;

    @Autowired
    private ConceptService conceptService;


    @Before
    public void before() {
        this.mockMvc = webAppContextSetup(webApplicationContext)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
        this.moduleRepo.deleteAllInBatch();
        this.conceptRepo.deleteAllInBatch();
    }

    @Test
    // [GET] /modules
    public void getAll() throws Exception {
        Module module = this.moduleRepo.save(new Module("name", "description"));
        Module module_bis = this.moduleRepo.save(new Module("name2", "description2"));

        mockMvc.perform(get("/modules")
                .contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(module.getId().intValue())))
                .andExpect(jsonPath("$[0].name", is(module.getName())))
                .andExpect(jsonPath("$[0].description", is(module.getDescription())))
                .andExpect(jsonPath("$[1].id", is(module_bis.getId().intValue())))
                .andExpect(jsonPath("$[1].name", is(module_bis.getName())))
                .andExpect(jsonPath("$[1].description", is(module_bis.getDescription())));
    }

    @Test
    // [GET] /modules/{id} - no module with this id
    public void moduleNotFound() throws Exception {
        mockMvc.perform(get("/modules/1")
                .contentType(contentType))
                .andExpect(status().isNotFound());
    }

    @Test
    // [GET] /modules/{id} - returning the given module
    public void getOneModule() throws Exception {
        Module module = this.moduleRepo.save(new Module("name", "description"));

        mockMvc.perform(get("/modules/"+module.getId())
                .contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(module.getId().intValue())))
                .andExpect(jsonPath("$.name", is(module.getName())))
                .andExpect(jsonPath("$.description", is(module.getDescription())));
    }

    @Test
    @WithMockUser(authorities = {"USER" , "ADMIN"})
    // [POST] /modules - all params, successfully created
    public void postAllParams_success() throws Exception {
        String name = "nom du module";
        String description = "description du module";

        mockMvc.perform(post("/modules")
                .contentType(contentType)
                .param("name", name)
                .param("description", description))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(name)))
                .andExpect(jsonPath("$.description", is(description))
        );

        // la réponse est good, on test maintenant si ça a bien été mis en BDD.
        List<Module> modules = moduleRepo.findByName(name);
        assertTrue("it should find a module with this name", modules.size() == 1);
    }

    @Test
    @WithMockUser(authorities = {"USER"})
    // [POST] /modules - all params, not admin
    public void postAllParams_forbidden() throws Exception {
        String name = "nom du module";
        String description = "description du module";

        mockMvc.perform(post("/modules")
                .contentType(contentType)
                .param("name", name)
                .param("description", description))
                .andExpect(status().isForbidden()
                );

        // la réponse est good, on test maintenant que rien n'a été persist
        List<Module> modules = moduleRepo.findByName(name);
        assertTrue("it shouldn't find any module with this name", modules.size() == 0);
    }

    @Test
    @WithMockUser(authorities = {"USER" , "ADMIN"})
    // [POST] /modules - missing params, nothing created
    public void postMissingParams() throws Exception {
        String name = "nom du module";

        mockMvc.perform(post("/modules")
                .contentType(contentType)
                .param("name", name))
                .andExpect(status().isBadRequest());

        // la réponse est good, on test maintenant que rien n'a été persist
        List<Module> modules = moduleRepo.findByName(name);
        assertTrue("it shouldn't find any module with this name", modules.size() == 0);
    }

    @Test
    // [POST] /modules/{id}/concepts - not admin
    public void addConcept_forbidden() throws Exception {
        String module_name = "nom du module";
        String module_description = "description du module";
        Module module = moduleRepo.save(new Module(module_name, module_description));

        mockMvc.perform(post("/modules/"+module.getId()+"/concepts")
                .contentType(contentType))
                .andExpect(status().isForbidden()
                );
    }

    @Test
    @WithMockUser(authorities = {"USER" , "ADMIN"})
    // [POST] /modules/{id}/concepts - all params, admin
    public void addConcept_OK() throws Exception {
        String module_name = "nom du module";
        String module_description = "description du module";
        Module module = moduleRepo.save(new Module(module_name, module_description));

        String concept_name = "nom du concept";
        Concept concept = conceptRepo.save(new Concept(concept_name));

        assertTrue("the module should not have nya concept", module.getConcepts().size() == 0);
        assertTrue("the concept should not have have any module", concept.getModules().size() == 0);

        mockMvc.perform(post("/modules/"+module.getId()+"/concepts")
                .contentType(contentType)
                .param("concept_id", concept.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.concepts[0].name", is(concept.getName()))
                );

        Concept new_concept = conceptService.get(concept.getId());
        Module new_module = moduleService.get(module.getId());
        assertTrue("the module should have a concept", new_module.getConcepts().size() > 0);
        assertTrue("the concept should have a module", new_concept.getModules().size() > 0);
    }

    @Test
    @WithMockUser(authorities = {"USER" , "ADMIN"})
    // [POST] /modules/{id}/concepts - module not found
    public void addConcept_moduleNotFound() throws Exception {
        String concept_name = "nom du concept";
        Concept concept = conceptRepo.save(new Concept(concept_name));

        mockMvc.perform(post("/modules/420/concepts")
                .contentType(contentType)
                .param("concept_id", concept.getId().toString()))
                .andExpect(status().isNotFound()
                );
    }

    @Test
    @WithMockUser(authorities = {"USER" , "ADMIN"})
    // [POST] /modules/{id}/concepts - concept not found
    public void addConcept_conceptNotFound_badRequest() throws Exception {
        String module_name = "nom du module";
        String module_description = "description du module";
        Module module = moduleRepo.save(new Module(module_name, module_description));
        mockMvc.perform(post("/modules/"+module.getId()+"/concepts")
                .contentType(contentType)
                .param("concept_id", "420"))
                .andExpect(status().isNotFound()
                );
    }

    @Test
    // [DELETE] /module/{id}
    @WithMockUser(authorities = {"ADMIN" })
    public void destroy() throws Exception {
        Module  module   = moduleRepo.save(new Module("name", "description"));
        Long id = module.getId();

        mockMvc.perform(delete("/modules/"+id)
                .contentType(contentType))
                .andExpect(status().isOk());

        assertFalse(this.moduleRepo.findById(id).isPresent());
    }

    @Test
    @WithMockUser(authorities = {"USER" , "ADMIN"})
    public void updateModule_OK() throws Exception {
        String name = "nom de la moduel";
        String new_name = "nouveau nombre";
        String description = "description de la module";
        String new_description = "nouvelle description";

        Module module = moduleRepo.save(new Module(name, description));

        assertTrue("the db should contain this module", moduleRepo.findByName(name) != null);

        mockMvc.perform(put("/modules/"+module.getId())
                .contentType(contentType)
                .param("name", new_name)
                .param("description", new_description))
                .andExpect(status().isOk());

        assertTrue("the module name should have been updated",
                moduleService.get(module.getId()).getName().equals(new_name));

        assertTrue("the module description should have been updated",
                moduleService.get(module.getId()).getDescription().equals(new_description));
    }

    @Test
    @WithMockUser(authorities = {"USER"})
    public void updateModule_forbidden() throws Exception {
        mockMvc.perform(put("/modules/1")
                .contentType(contentType)
                .param("name", "blabla")
                .param("description", "description"))
                .andExpect(status().isForbidden());
    }

}