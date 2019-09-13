package io.riggo.web;

import io.riggo.data.domain.LoadPipeline;
import io.riggo.data.services.LoadPipelineService;
import io.riggo.data.services.ShipperService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(LoadPipelineSummaryController.class)
public class LoadPipelineSummaryControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private LoadPipelineService loadPipelineService;

    @MockBean
    private ShipperService shipperService;

    @MockBean
    private AuthenticationFacade authenticationFacade;


    private static Logger logger = LoggerFactory.getLogger(LoadPipelineSummaryControllerTest.class);

    @WithMockUser(value = "spring", authorities = {"read:loadPipeline"})
    @Test
    public void getPipelineSummaryHappyPathSuperAdmin() throws Exception {

        LoadPipeline loadPipeline = new LoadPipeline();
        loadPipeline.setId(1);
        loadPipeline.setCount(1);

        List<LoadPipeline> loadPipelines = new ArrayList<>();
        loadPipelines.add(loadPipeline);

        given(authenticationFacade.isSuperAdmin()).willReturn(true);
        given(authenticationFacade.getSiteId()).willReturn(100);
        given(loadPipelineService.findPipelineSummaryBySiteId(100)).willReturn(java.util.Optional.of(loadPipelines));

        MvcResult result = mvc.perform(get(Paths.API_VERSION + Paths.LOAD_PIPELINE_SUMMARY)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(3)))
                .andExpect(jsonPath("$.data[0].subStatuses[0].id", is(loadPipeline.getId())))
                .andExpect(jsonPath("$.data[0].subStatuses[0].count", is(loadPipeline.getCount())))
                .andReturn();

        String content = result.getResponse().getContentAsString();
        logger.error(content);
    }


    @Test
    public void getPipelineSummaryUnauthenticated() throws Exception {
        MvcResult result = mvc.perform(get(Paths.API_VERSION + Paths.LOAD + "/1")
                .contentType(APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        logger.debug(content);
    }


    @WithMockUser(value = "spring1")
    @Test
    public void getPipelineSummaryRequiresReadLoadPipelinePermission() throws Exception {
        MvcResult result = mvc.perform(get(Paths.API_VERSION  + Paths.LOAD_PIPELINE_SUMMARY)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        logger.error(content);
    }

}
