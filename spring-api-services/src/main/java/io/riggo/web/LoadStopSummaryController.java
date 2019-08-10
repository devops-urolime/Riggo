package io.riggo.web;

import io.riggo.data.domain.LoadStopArrivalStatus;
import io.riggo.data.domain.LoadStopSummary;
import io.riggo.data.domain.LoadStopType;
import io.riggo.data.domain.ResourceType;
import io.riggo.data.exception.ResourceNotFoundException;
import io.riggo.data.services.LoadStopService;
import io.riggo.web.response.BaseAPIResponse;
import io.riggo.web.response.IdNameCountData;
import io.riggo.web.response.LoadStopSummaryData;
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
public class LoadStopSummaryController {

    @Autowired
    private LoadStopService loadStopService;

    @GetMapping(value = Paths.LOAD_STOP_SUMMARY, produces = "application/json")
    @ResponseBody
    //@Cacheable(value = "menus", key = "#m0", unless = "#result == null")
    public BaseAPIResponse<LoadStopSummaryData> getPipelineSummary() throws ResourceNotFoundException{
        //TODO: obtain siteId from JWT Token.  Make this Resource Context Aware.
        //TODO: Is this how we cache a list?  How do we expire the cache?
        //TODO: Find the user's association to a shipper.

        Optional<List<LoadStopSummary>> loadStopSummaryList = loadStopService.findStopSummaryBySiteIdShipperId(100, 1);
        if (loadStopSummaryList.isPresent()) {
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