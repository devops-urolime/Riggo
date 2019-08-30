package io.riggo.web;

import io.riggo.data.domain.*;
import io.riggo.data.exception.ResourceNotFoundException;
import io.riggo.data.services.LoadStopService;
import io.riggo.data.services.ShipperService;
import io.riggo.web.response.BaseAPIResponse;
import io.riggo.web.response.IdNameCountData;
import io.riggo.web.response.LoadStopSummaryData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = Paths.API_VERSION)
public class LoadStopSummaryController {

    @Autowired
    private LoadStopService loadStopService;

    @Autowired
    private AuthenticationFacade authenticationFacade;

    @Autowired
    private ShipperService shipperService;


    @GetMapping(value = Paths.LOAD_STOP_SUMMARY, produces = "application/json")
    @PreAuthorize("hasAuthority('read:load')")
    @ResponseBody
    //@Cacheable(value = "menus", key = "#m0", unless = "#result == null")
    public BaseAPIResponse<LoadStopSummaryData> getPipelineSummary() throws ResourceNotFoundException{
        Optional<List<LoadStopSummary>> loadStopSummaryList = null;
        if(authenticationFacade.isSuperAdmin() || authenticationFacade.isSiteAdmin() ) {
            loadStopSummaryList = loadStopService.findStopSummaryBySiteId(authenticationFacade.getSiteId());
        }
        else if(authenticationFacade.isShipperExecutive())
        {
            Optional<Shipper> shipper = shipperService.findByEmailAndSiteId(authenticationFacade.getUsername(), authenticationFacade.getSiteId());
            if(shipper.isPresent()) {
                loadStopSummaryList = loadStopService.findStopSummaryBySiteIdShipperId(authenticationFacade.getSiteId(), shipper.get().getId());
            }
        }else{
            throw new ResourceNotFoundException(ResourceType.LOAD_PIPELINE, authenticationFacade.getSiteId());
        }

        if (loadStopSummaryList != null && loadStopSummaryList.isPresent()) {
            List<LoadStopSummaryData> loadStopSummaryDataList = new ArrayList<>();
            for (LoadStopType loadStopType : LoadStopType.values()) {
                if (loadStopType.getColVal() > 0) {
                    LoadStopSummaryData loadStopSummaryData = new LoadStopSummaryData();
                    loadStopSummaryData.setId(loadStopType.getColVal());
                    loadStopSummaryData.setName(loadStopType.getDisplayName());

                    List<IdNameCountData> idNameCountDataList = new ArrayList<>();
                    for (LoadStopArrivalStatus loadStopArrivalStatus : LoadStopArrivalStatus.values()) {
                        IdNameCountData idNameCountData = new IdNameCountData();
                        idNameCountData.setId(loadStopArrivalStatus.getColVal());
                        idNameCountData.setName(loadStopArrivalStatus.getDisplayName());
                        idNameCountData.setCount(0);
                        for (LoadStopSummary loadStopSummary : loadStopSummaryList.get()) {
                            if (loadStopSummary.getType() == loadStopType.getColVal() &&
                                    loadStopSummary.getArrivalStatus() == loadStopArrivalStatus.getColVal()) {
                                idNameCountData.setCount(loadStopSummary.getCount());
                            }
                        }
                        idNameCountDataList.add(idNameCountData);
                    }
                    loadStopSummaryData.setData(idNameCountDataList);
                    loadStopSummaryDataList.add(loadStopSummaryData);
                }
            }
            BaseAPIResponse<LoadStopSummaryData> baseAPIResponse = new BaseAPIResponse();
            baseAPIResponse.setData(loadStopSummaryDataList);
            return baseAPIResponse;
        }
        throw new ResourceNotFoundException(ResourceType.LOAD_STOP_SUMMARY, 0);
    }
}