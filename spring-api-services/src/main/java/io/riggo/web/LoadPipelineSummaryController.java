package io.riggo.web;

import io.riggo.data.domain.*;
import io.riggo.data.exception.ResourceNotFoundException;
import io.riggo.data.services.LoadPipelineService;
import io.riggo.data.services.ShipperService;
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
public class LoadPipelineSummaryController {

    @Autowired
    private LoadPipelineService loadPipelineService;

    @Autowired
    private AuthenticationFacade authenticationFacade;

    @Autowired
    private ShipperService shipperService;

    @GetMapping(value = Paths.LOAD_PIPELINE_SUMMARY, produces = "application/json")
    @ResponseBody
    public BaseAPIResponse<LoadPipelineData> getPipelineSummary() throws ResourceNotFoundException{
        Optional<List<LoadPipeline>> loadPipelineList = null;
        if(authenticationFacade.isSuperAdmin() || authenticationFacade.isSiteAdmin() ) {
            loadPipelineList = loadPipelineService.findPipelineSummaryBySiteId(authenticationFacade.getSiteId());
        }
        else if(authenticationFacade.isShipperExecutive())
        {
            Optional<Shipper> shipper = shipperService.findByEmailAndSiteId(authenticationFacade.getUsername(), authenticationFacade.getSiteId());
            if(shipper.isPresent()) {
                loadPipelineList = loadPipelineService.findPipelineSummaryBySiteIdShipperId(authenticationFacade.getSiteId(), shipper.get().getId());
            }
        }else{
            throw new ResourceNotFoundException(ResourceType.LOAD_PIPELINE, authenticationFacade.getSiteId());
        }

        if (loadPipelineList != null && loadPipelineList.isPresent()) {
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
                                if (loadPipeline.getId() == loadSubStatus.getColVal()) {
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