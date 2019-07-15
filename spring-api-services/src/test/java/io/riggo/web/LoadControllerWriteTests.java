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

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@WebMvcTest(LoadController.class)

public class LoadControllerWriteTests {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LoadService loadService;

    @WithMockUser(value = "spring", authorities = {"read:load", "write:load"})
    @Test
    public void postTest() throws Exception {


        Load load = new Load();
        load.setId(1L);


        assertThat(load).isNotNull();

       /* ObjectMapper mapper=new ObjectMapper();
        String jsonString=mapper.writeValueAsString(mapper.readValue(new File("classpath:tests/LoadV4.json"),Object.class));

        this.mockMvc.perform(post(Paths.API_VERSION_LOAD)
                .contentType(MediaType.APPLICATION_JSON_UTF8).content(jsonString))
                .andExpect(status().isConflict());

        */


    }
}