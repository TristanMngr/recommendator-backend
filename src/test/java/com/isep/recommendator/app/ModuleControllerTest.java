package com.isep.recommendator.app;

import com.isep.recommendator.app.model.Module;
import com.isep.recommendator.app.repository.ModuleRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import java.nio.charset.Charset;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.util.AssertionErrors.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class ModuleControllerTest {

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;

    @Autowired
    private ModuleRepository moduleRepo;


    @Autowired
    private WebApplicationContext webApplicationContext;

    //TODO generer un token a réutiliser pour les tests :)

    @Before
    public void before() {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
        this.moduleRepo.deleteAllInBatch();
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
    // [POST] /modules - all params, successfully created
    public void postAllParams() throws Exception {
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

}