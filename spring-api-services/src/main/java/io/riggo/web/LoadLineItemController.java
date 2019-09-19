package io.riggo.web;

import io.riggo.data.domain.Load;
import io.riggo.data.domain.LoadLineItem;
import io.riggo.data.domain.ResourceType;
import io.riggo.data.exception.ResourceNotFoundException;
import io.riggo.data.services.LoadLineItemService;
import io.riggo.data.services.LoadService;
import io.riggo.web.integration.SalesforceRevenovaConstants;
import io.riggo.web.integration.parser.SalesforceRevenovaRequestBodyParserForPatchLoadLoadLineItem;
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
public class LoadLineItemController {

    Logger logger = LoggerFactory.getLogger(LoadLineItemController.class);

    @Autowired
    private LoadService loadService;

    @Autowired
    private LoadLineItemService loadLineItemService;

    @Autowired
    private SalesforceRevenovaRequestBodyParserForPatchLoadLoadLineItem salesforceRevenovaRequestBodyParserForPatchLoadLoadLineItem;

    @Autowired
    private AuthenticationFacade authenticationFacade;

    @PutMapping(value = Paths.LOAD_LINE_ITEM, produces = "application/json")
    @PreAuthorize("hasAuthority('write:load')")
    public NestedBaseAPIResponse<?> putLoadLineItem(@RequestBody Map<String, Object> dataHashMap) throws ResourceNotFoundException{
        return saveLoadLineItem(dataHashMap, null);
    }

    @PutMapping(value = Paths.LOAD_LOADID_PARAM_LINE_ITEM, produces = "application/json")
    @PreAuthorize("hasAuthority('write:load')")
    public NestedBaseAPIResponse<?> putLoadLineItem(@RequestBody Map<String, Object> dataHashMap, @PathVariable final Integer loadId) throws ResourceNotFoundException{
        return saveLoadLineItem(dataHashMap, loadId);
    }

    private NestedBaseAPIResponse<LoadLineItem> saveLoadLineItem(Map<String, Object> dataHashMap, Integer loadId) {
        List<LoadLineItem> loadLineItemList = salesforceRevenovaRequestBodyParserForPatchLoadLoadLineItem.resolveLoadLineItemsList(dataHashMap);
        List<BaseAPIResponse<LoadLineItem>> loadLineItemsBaseAPIResponses = loadLineItemList
                .stream()
                .map(loadLineItem -> {
                    BaseAPIResponse<LoadLineItem> loadLineItemBaseAPIResponse = new BaseAPIResponse<>();
                    if (StringUtils.isNotBlank(loadLineItem.getLoadExtSysId())) {
                        Optional<Load> optionalLoad = loadService.findByExtSysId(loadLineItem.getLoadExtSysId(), authenticationFacade.getSiteId());
                        if (optionalLoad.isPresent() && (loadId == null || optionalLoad.get().getId() == loadId)) {
                            if (StringUtils.isNotBlank(loadLineItem.getExtSysId())) {
                                Optional<LoadLineItem> loadLineItemFromDb = loadLineItemService.findByExtSysId(loadLineItem.getExtSysId(), authenticationFacade.getSiteId());
                                if (loadLineItemFromDb.isPresent()) {
                                    BeanUtils.copyProperties(loadLineItemFromDb.get(), loadLineItem, SalesforceRevenovaConstants.PUT_LOAD_LINE_ITEM_IGNORE_PROPERTIES);
                                }
                                if(optionalLoad.isPresent()) {
                                    loadLineItem.setLoadId(optionalLoad.get().getId());
                                }
                                loadLineItemService.save(loadLineItem);
                                loadLineItemBaseAPIResponse.addData(loadLineItem);
                                return loadLineItemBaseAPIResponse;
                            }
                        }
                    }
                    loadLineItemBaseAPIResponse.setStatus(HttpStatus.NOT_FOUND.value());
                    loadLineItemBaseAPIResponse.setMessage(new ResourceNotFoundException(ResourceType.LOAD, loadLineItem.getLoadExtSysId()).getMessage());
                    return loadLineItemBaseAPIResponse;
                }).collect(Collectors.toList());

        NestedBaseAPIResponse<LoadLineItem> loadLineItemNestedBaseAPIResponse = new NestedBaseAPIResponse<>();
        loadLineItemNestedBaseAPIResponse.setSuccess(Math.toIntExact(loadLineItemsBaseAPIResponses.stream().filter(loadLineItemBaseAPIResponse -> loadLineItemBaseAPIResponse.getStatus() == 200).count()));
        loadLineItemNestedBaseAPIResponse.setFailures((loadLineItemsBaseAPIResponses.size()) - loadLineItemNestedBaseAPIResponse.getSuccess());
        loadLineItemNestedBaseAPIResponse.setData(loadLineItemsBaseAPIResponses);

        return loadLineItemNestedBaseAPIResponse;
    }

}
