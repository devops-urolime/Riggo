package io.riggo.web;

import io.riggo.data.domain.LoadPipeline;
import io.riggo.data.domain.LoadStatus;
import io.riggo.data.domain.LoadSubStatus;
import io.riggo.data.domain.ResourceType;
import io.riggo.data.exception.ResourceNotFoundException;
import io.riggo.data.services.LoadPipelineService;
import io.riggo.web.response.BaseAPIResponse;
import io.riggo.web.response.LoadPipelineData;
import io.riggo.web.response.LoadPipelineStatusData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = Paths.API_VERSION)
public class LoadPipelineController {

    @Autowired
    private LoadPipelineService loadPipelineService;

    @GetMapping(value = Paths.LOAD_PIPELINE_SUMMARY, produces = "application/json")
    @ResponseBody
    //@Cacheable(value = "menus", key = "#m0", unless = "#result == null")
    public BaseAPIResponse<LoadPipelineData> getPipelineSummary() {
        //TODO: obtain siteId from JWT Token.  Make this Resource Context Aware.
        //TODO: Is this how we cache a list?  How do we expire the cache?
        //TODO: Find the user's association to a shipper.

        Optional<List<LoadPipeline>> loadPipelineList = loadPipelineService.findPipelineSummaryBySiteIdShipperId(100l, 1l);
        if (loadPipelineList.isPresent()) {
            List<LoadPipelineData> loadPipelineDataList = new ArrayList<>();
            for (LoadStatus loadStatus : LoadStatus.values()) {
                if (loadStatus.getColVal() > 0) {
                    LoadPipelineData loadPipelineData = new LoadPipelineData();
                    loadPipelineData.setId(loadStatus.getColVal());
                    loadPipelineData.setName(loadStatus.getDisplayName());
                    List<LoadPipelineStatusData> loadPipelineStatusDataList = new ArrayList<>();
                    int statusCount = 0;

                    for (LoadSubStatus loadSubStatus : LoadSubStatus.values()) {
                        if (loadSubStatus.getLoadStatus() == loadStatus) {

                            LoadPipelineStatusData loadPipelineStatusData = new LoadPipelineStatusData();
                            loadPipelineStatusData.setId(loadSubStatus.getColVal());
                            loadPipelineStatusData.setName(loadSubStatus.getDisplayName());
                            loadPipelineStatusData.setCount(0);
                            for (LoadPipeline loadPipeline : loadPipelineList.get()) {
                                if (loadPipeline.getId() == loadStatus.getColVal()) {
                                    loadPipelineStatusData.setCount(loadPipeline.getCount());
                                    statusCount = statusCount + loadPipeline.getCount();
                                }
                            }
                            loadPipelineStatusDataList.add(loadPipelineStatusData);
                        }
                    }
                    loadPipelineData.setCount(statusCount);
                    loadPipelineDataList.add(loadPipelineData);
                    loadPipelineData.setSubStatuses(loadPipelineStatusDataList);
                }
            }
            BaseAPIResponse<LoadPipelineData> baseAPIResponse = new BaseAPIResponse();
            baseAPIResponse.setData(loadPipelineDataList);
            return baseAPIResponse;
        }
        throw new ResourceNotFoundException(ResourceType.LOAD_PIPELINE, 0);
    }
}