package io.riggo.web;

import io.riggo.data.domain.Load;
import io.riggo.data.services.LoadService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(LoadController.class)
public class LoadControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private LoadService loadService;

    @WithMockUser(value = "spring", authorities = {"read:load"})
    @Test
    public void getLoadById() throws Exception {
        Load load = new Load();
        load.setId(1l);
        load.setName("load");

        given(loadService.findById(1l)).willReturn(java.util.Optional.of(load));

        MvcResult result = mvc.perform(get(Paths.API_VERSION_LOAD + "/1")
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                //.andExpect(jsonPath("$.load.id").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.load.id", is(1)))
                .andReturn();
        //String content = result.getResponse().getContentAsString();
    }


    @Test
    public void getLoadByIdUnauthenticated() throws Exception {
        Load load = new Load();
        load.setId(1l);
        load.setName("load");

        given(loadService.findById(1l)).willReturn(java.util.Optional.of(load));

        mvc.perform(get(Paths.API_VERSION_LOAD + "/1")
                .contentType(APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andReturn();
    }


    @WithMockUser(value = "spring")
    @Test
    public void getLoadByIdRequiresReadLoadPermission() throws Exception {
        Load load = new Load();
        load.setId(1l);
        load.setName("load");

        given(loadService.findById(1l)).willReturn(java.util.Optional.of(load));

        mvc.perform(get(Paths.API_VERSION_LOAD + "/1")
                .contentType(APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andReturn();
    }
}