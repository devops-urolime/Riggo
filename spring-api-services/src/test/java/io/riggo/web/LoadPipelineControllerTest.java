package io.riggo.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import io.riggo.data.domain.LoadPipeline;
import io.riggo.data.services.LoadPipelineService;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.BDDMockito.given;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(LoadPipelineController.class)
public class LoadPipelineControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private LoadPipelineService loadPipelineService;

    private static Logger logger = LoggerFactory.getLogger(LoadPipelineControllerTest.class);

    @WithMockUser(value = "spring", authorities = {"read:loadPipeline"})
    @Test
    public void getPipelineSummary() throws Exception {
        LoadPipeline loadPipeline = new LoadPipeline();
        loadPipeline.setPending(20);

        given(loadPipelineService.findPipelineSummaryBySiteIdShipperId(100l, 1l)).willReturn(java.util.Optional.of(loadPipeline));

        MvcResult result = mvc.perform(get(Paths.API_VERSION_LOAD_PIPELINE_SUMMARY)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].pending", is(loadPipeline.getPending())))
                .andReturn();
        String content = result.getResponse().getContentAsString();
        logger.error(content);
    }


    @WithMockUser(value = "spring1")
    @Test
    public void getPipelineSummaryRequiresReadLoadPipelinePermission() throws Exception {
        LoadPipeline loadPipeline = new LoadPipeline();
        loadPipeline.setPending(20);

        given(loadPipelineService.findPipelineSummaryBySiteIdShipperId(100l, 1l)).willReturn(java.util.Optional.of(loadPipeline));

        MvcResult result = mvc.perform(get(Paths.API_VERSION_LOAD_PIPELINE_SUMMARY)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        logger.error(content);
    }

    @Test
    public void getPipelineSummaryUnauthenticated() throws Exception {
        LoadPipeline loadPipeline = new LoadPipeline();
        loadPipeline.setPending(20);

        given(loadPipelineService.findPipelineSummaryBySiteIdShipperId(100l, 1l)).willReturn(java.util.Optional.of(loadPipeline));

        MvcResult result = mvc.perform(get(Paths.API_VERSION_LOAD_PIPELINE_SUMMARY)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        logger.error(content);
    }
}
