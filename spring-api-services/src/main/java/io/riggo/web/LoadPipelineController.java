package io.riggo.web;

import io.riggo.data.domain.LoadPipeline;
import io.riggo.data.domain.Menu;
import io.riggo.data.services.LoadPipelineService;
import io.riggo.web.response.BaseAPIResponse;
import io.riggo.web.response.BaseAPIResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = Paths.API_VERSION)
public class LoadPipelineController {

    @Autowired
    private LoadPipelineService loadPipelineService;

    @GetMapping(value = Paths.LOAD_PIPELINE_SUMMARY, produces = "application/json")
    @ResponseBody
    //@Cacheable(value = "menus", key = "#m0", unless = "#result == null")
    public BaseAPIResponse<Menu> getPipelineSummary() {
        //TODO: obtain siteId from JWT Token.  Make this Resource Context Aware.
        //TODO: Is this how we cache a list?  How do we expire the cache?
        //TODO: Find the user's association to a shipper.
        return new BaseAPIResponseWrapper<LoadPipeline>().wrapForResponse(loadPipelineService.findPipelineSummaryBySiteIdShipperId(100l, 1l));
    }
}