package io.riggo.web;

import io.riggo.data.domain.Load;
import io.riggo.data.domain.LoadStop;
import io.riggo.data.domain.ResourceType;
import io.riggo.data.exception.ResourceAlreadyExistsException;
import io.riggo.data.exception.ResourceNotFoundException;
import io.riggo.data.services.LoadService;
import io.riggo.data.services.LoadStopService;
import io.riggo.web.integration.SalesforceRevenovaConstants;
import io.riggo.web.integration.parser.SalesforceRevenovaRequestBodyParserForPatchLoadStop;
import io.riggo.web.response.BaseAPIResponse;
import io.riggo.web.response.NestedBaseAPIResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@RequestMapping(value = Paths.API_VERSION)
public class LoadStopController {

    Logger logger = LoggerFactory.getLogger(LoadStopController.class);

    @Autowired
    private LoadService loadService;

    @Autowired
    private LoadStopService loadStopService;

    @Autowired
    private SalesforceRevenovaRequestBodyParserForPatchLoadStop salesforceRevenovaRequestBodyParserForPatchLoadStop;

    @Autowired
    private AuthenticationFacade authenticationFacade;



//    @PostMapping(value = "/load/stop", produces = "application/json")
//    @PreAuthorize("hasAuthority('write:load')")
//    public NestedBaseAPIResponse<?> postLoadStop(@RequestBody Map<String, Object> dataHashMap) throws ResourceNotFoundException{
//        return saveLoadStop(dataHashMap, true);
//    }

    @PutMapping(value = "/load/stop", produces = "application/json")
    @PreAuthorize("hasAuthority('write:load')")
    public NestedBaseAPIResponse<?> putLoadStop(@RequestBody Map<String, Object> dataHashMap) throws ResourceNotFoundException{
        return saveLoadStop(dataHashMap);
    }

    private NestedBaseAPIResponse<LoadStop> saveLoadStop(Map<String, Object> dataHashMap){
        List<LoadStop> loadStopList = salesforceRevenovaRequestBodyParserForPatchLoadStop.resolveLoadStopsList(dataHashMap);
        List<BaseAPIResponse<LoadStop>> loadStopListBaseAPIResponses = loadStopList
                .stream()
                .map(loadStop -> {
                    BaseAPIResponse<LoadStop> loadStopBaseAPIResponse = new BaseAPIResponse<>();
                    if(StringUtils.isNotBlank(loadStop.getLoadExtSysId())) {
                        Optional<Load> optionalLoad = loadService.findByExtSysId(loadStop.getLoadExtSysId(), authenticationFacade.getSiteId());
                        if (optionalLoad.isPresent()) {
                            if (StringUtils.isNotBlank(loadStop.getExtSysId())) {
                                Optional<LoadStop> loadStopFromDb = loadStopService.findByExtSysId(loadStop.getExtSysId(), authenticationFacade.getSiteId());
                                if (loadStopFromDb.isPresent()) {
                                    BeanUtils.copyProperties(loadStop, loadStopFromDb, SalesforceRevenovaConstants.PATCH_LOAD_LOAD_STOP_IGNORE_PROPERTIES);
                                }
                                loadStopService.save(loadStop);
                                loadStopBaseAPIResponse.addData(loadStop);
                                return loadStopBaseAPIResponse;
                            }
                        }
                    }

                    loadStopBaseAPIResponse.setStatus(HttpStatus.NOT_FOUND.value());
                    loadStopBaseAPIResponse.setMessage(new ResourceNotFoundException(ResourceType.LOAD, loadStop.getLoadExtSysId()).getMessage());
                    return loadStopBaseAPIResponse;
                }).collect(Collectors.toList());
        NestedBaseAPIResponse<LoadStop> loadStopNestedBaseAPIResponse = new NestedBaseAPIResponse<>();
        loadStopNestedBaseAPIResponse.setSuccess(Math.toIntExact(loadStopListBaseAPIResponses.stream().filter(loadLineItemBaseAPIResponse -> loadLineItemBaseAPIResponse.getStatus() == 200).count()));
        loadStopNestedBaseAPIResponse.setFailures((loadStopListBaseAPIResponses.size()) - loadStopNestedBaseAPIResponse.getSuccess());
        loadStopNestedBaseAPIResponse.setData(loadStopListBaseAPIResponses);
        return loadStopNestedBaseAPIResponse;
    }

}