package io.riggo.web;

import com.google.common.hash.Hashing;
import io.riggo.data.domain.*;
import io.riggo.data.exception.ResourceAlreadyExistsException;
import io.riggo.data.exception.ResourceNotFoundException;
import io.riggo.data.services.*;
import io.riggo.web.integration.SalesforceRevenovaConstants;
import io.riggo.web.integration.parser.SalesforceRevenovaRequestBodyParserForPatchLoadLoadLineItem;
import io.riggo.web.integration.parser.SalesforceRevenovaRequestBodyParserForPatchLoadStop;
import io.riggo.web.integration.parser.SalesforceRevenovaRequestBodyParserPostPutLoad;
import io.riggo.web.response.BaseAPIResponse;
import io.riggo.web.response.LoadAPIResponse;
import io.riggo.web.response.LoadResponse;
import io.riggo.web.response.NestedBaseAPIResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@RequestMapping(value = Paths.API_VERSION)
public class LoadController {

    Logger logger = LoggerFactory.getLogger(LoadController.class);

    @Autowired
    private LoadService loadService;

    @Autowired
    private ShipperService shipperService;

    @Autowired
    private EquipmentTypeService equipmentTypeService;

    @Autowired
    private LoadStopService loadStopService;

    @Autowired
    private LoadLineItemService loadLineItemService;

    @Autowired
    private SalesforceRevenovaRequestBodyParserPostPutLoad salesforceRevenovaRequestBodyParserPostPutLoad;

    @Autowired
    private SalesforceRevenovaRequestBodyParserForPatchLoadLoadLineItem salesforceRevenovaRequestBodyParserForPatchLoadLoadLineItem;

    @Autowired
    private SalesforceRevenovaRequestBodyParserForPatchLoadStop salesforceRevenovaRequestBodyParserForPatchLoadStop;


    @GetMapping(value = Paths.LOAD + "/{id}")//., produces = "application/json")
    @ResponseBody
    @Cacheable(value = "loads", key = "#p0", unless = "#result == null")
    public LoadResponse getLoadById(@PathVariable("id") Long id, Authentication authentication) {
        // TODO: Remove this line in a future is just a demo authentication.
        logger.debug(authentication.toString());
        Optional<Load> load = loadService.findById(id);
        return new LoadResponse(load.get());
    }


    @Cacheable(value = "loadsEXT", key = "#p0")//for now
    @GetMapping("/load/external/{extSysId}")
    public Optional<Load> getLoadByExternalId(@PathVariable("extSysId") String extSysId, @RequestParam(required = false, name = "external", value = "false") Boolean findByExternal) {
        if (findByExternal) {
            return loadService.findByExtSysId(extSysId);
        } else {
            return loadService.findById(Long.valueOf(extSysId));
        }
    }


    @PostMapping(path = "/load", produces = "application/json")
    public LoadAPIResponse postLoad(@RequestBody Map<String, Object> dataHashMap) {
        //TODO: resolve parsers based on site and integration
        Load load = salesforceRevenovaRequestBodyParserPostPutLoad.resolveLoad(dataHashMap);

        Optional<Load> checkLoadExists = loadService.findByExtSysId(load.getExtSysId());
        if (checkLoadExists.isPresent()) {
            throw new ResourceAlreadyExistsException(ResourceType.LOAD, checkLoadExists.get().getId());
        }

        Shipper shipper = salesforceRevenovaRequestBodyParserPostPutLoad.resolveShipper(dataHashMap);
        persistShipper(shipper, load);

        EquipmentType equipmentType = salesforceRevenovaRequestBodyParserPostPutLoad.resolveEquipmentType(dataHashMap);
        if (StringUtils.isNotBlank(equipmentType.getExtSysId())) {
            Optional<EquipmentType> checkEquipmentTypeExists = equipmentTypeService.findByExtSysId(equipmentType.getExtSysId());
            if (checkEquipmentTypeExists.isPresent()) {
                EquipmentType checkedEquipmentType = checkEquipmentTypeExists.get();
                if (StringUtils.isNotBlank(equipmentType.getName()) && !StringUtils.equals(checkedEquipmentType.getName(), equipmentType.getName())) {
                    BeanUtils.copyProperties(checkEquipmentTypeExists.get(), equipmentType, SalesforceRevenovaConstants.POST_PUT_SHIPPER_IGNORE_PROPERTIES);
                    equipmentTypeService.save(equipmentType);
                }
            }else{
                equipmentTypeService.save(equipmentType);
            }
        }
        load.setEquipmentTypeId(equipmentType.getId());
        load = loadService.save(load);

        LoadStop firstStop = salesforceRevenovaRequestBodyParserPostPutLoad.resolveFirstStop(dataHashMap);
        LoadStop lastStop = salesforceRevenovaRequestBodyParserPostPutLoad.resolveLastStop(dataHashMap);
        if (StringUtils.isNotBlank(firstStop.getExtSysId())) {
            Optional<LoadStop> checkFirstStopExists = loadStopService.findByExtSysId(firstStop.getExtSysId());
            if (!checkFirstStopExists.isPresent()) {
                firstStop.setLoadId(load.getId());
                loadStopService.save(firstStop);
            }
        }
        if (StringUtils.isNotBlank(lastStop.getExtSysId())) {
            Optional<LoadStop> checkLastStopExists = loadStopService.findByExtSysId(lastStop.getExtSysId());
            if (!checkLastStopExists.isPresent()) {
                lastStop.setLoadId(load.getId());
                loadStopService.save(lastStop);
            }
        }

        LoadAPIResponse loadAPIResponse = new LoadAPIResponse();
        loadAPIResponse.addData(load);
        loadAPIResponse.setHashId(Hashing.sha256()
                .hashString(load.getExtSysId() + load.getId(), StandardCharsets.UTF_8)
                .toString());

        return loadAPIResponse;
    }


    @PutMapping(value = "/load", produces = "application/json")
    public LoadAPIResponse updateLoad(@RequestBody Map<String, Object> dataHashMap) {
        Load resolvedLoad = salesforceRevenovaRequestBodyParserPostPutLoad.resolveLoad(dataHashMap);
        Optional<Load> loadFromDb = loadService.findByExtSysId(resolvedLoad.getExtSysId());
        if (!loadFromDb.isPresent()) {
            throw new ResourceNotFoundException(ResourceType.LOAD, resolvedLoad.getExtSysId());
        }

        Shipper shipper = salesforceRevenovaRequestBodyParserPostPutLoad.resolveShipper(dataHashMap);
        persistShipper(shipper, resolvedLoad);

        //
        Load existingLoad = loadFromDb.get();
        BeanUtils.copyProperties(resolvedLoad, existingLoad, SalesforceRevenovaConstants.POST_PUT_LOAD_IGNORE_PROPERTIES);
        existingLoad = loadService.save(existingLoad);

        LoadAPIResponse loadAPIResponse = new LoadAPIResponse();
        loadAPIResponse.addData(existingLoad);
        loadAPIResponse.setHashId(Hashing.sha256()
                .hashString(existingLoad.getExtSysId() + existingLoad.getId(), StandardCharsets.UTF_8)
                .toString());

        return loadAPIResponse;
    }

    private void persistShipper(Shipper shipper, Load load) {
        if (StringUtils.isNotBlank(shipper.getExtSysId())) {
            Optional<Shipper> checkShipperExists = shipperService.findByExtSysId(shipper.getExtSysId());
            if (checkShipperExists.isPresent()) {
                BeanUtils.copyProperties(checkShipperExists.get(), shipper, SalesforceRevenovaConstants.POST_PUT_SHIPPER_IGNORE_PROPERTIES);
            }
            shipperService.save(shipper);
            load.setShipperId(shipper.getId());
        }
    }

    @PatchMapping(value = "/load", produces = "application/json")
    public NestedBaseAPIResponse<?> patchLoad(@RequestBody Map<String, Object> dataHashMap) {
        if(dataHashMap.containsKey("LineItems")) {
            List<LoadLineItem> loadLineItemList = salesforceRevenovaRequestBodyParserForPatchLoadLoadLineItem.resolveLoadLineItemsList(dataHashMap);

            List<BaseAPIResponse<LoadLineItem>> loadLineItemsBaseAPIResponses = loadLineItemList
                .stream()
                .map(loadLineItem -> {
                    BaseAPIResponse<LoadLineItem> loadLineItemBaseAPIResponse = new BaseAPIResponse<>();
                    if(StringUtils.isNotBlank(loadLineItem.getLoadExtSysId())){
                        Optional<Load> optionalLoad = loadService.findByExtSysId(loadLineItem.getLoadExtSysId());
                        if(optionalLoad.isPresent()) {
                            if(StringUtils.isNotBlank(loadLineItem.getExtSysId())) {
                                Optional<LoadLineItem> loadLineItemFromDb = loadLineItemService.findByExtSysId(loadLineItem.getExtSysId());
                                if (loadLineItemFromDb.isPresent()) {
                                    BeanUtils.copyProperties(loadLineItem, loadLineItemFromDb, SalesforceRevenovaConstants.PATCH_LOAD_LOAD_LINE_ITEM_IGNORE_PROPERTIES);
                                }
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
            loadLineItemNestedBaseAPIResponse.setFailures((loadLineItemsBaseAPIResponses.size() + 1) - loadLineItemNestedBaseAPIResponse.getSuccess());
            loadLineItemNestedBaseAPIResponse.setData(loadLineItemsBaseAPIResponses);

            return loadLineItemNestedBaseAPIResponse;
        }
        else if(dataHashMap.containsKey("LoadStops")){
            List<LoadStop> loadStopList = salesforceRevenovaRequestBodyParserForPatchLoadStop.resolveLoadStopsList(dataHashMap);
            List<BaseAPIResponse<LoadStop>> loadStopListBaseAPIResponses = loadStopList
                .stream()
                .map(loadStop -> {
                    BaseAPIResponse<LoadStop> loadStopBaseAPIResponse = new BaseAPIResponse<>();
                    if(StringUtils.isNotBlank(loadStop.getLoadExtSysId())) {
                        Optional<Load> optionalLoad = loadService.findByExtSysId(loadStop.getLoadExtSysId());
                        if (optionalLoad.isPresent()) {
                            if (StringUtils.isNotBlank(loadStop.getExtSysId())) {
                                Optional<LoadStop> loadStopFromDb = loadStopService.findByExtSysId(loadStop.getExtSysId());
                                if (loadStopFromDb.isPresent()) {
                                    BeanUtils.copyProperties(loadStop, loadStopFromDb, SalesforceRevenovaConstants.PATCH_LOAD_LOAD_STOP_IGNORE_PROPERTIES);
                                }
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
            loadStopNestedBaseAPIResponse.setFailures((loadStopListBaseAPIResponses.size() + 1) - loadStopNestedBaseAPIResponse.getSuccess());
            loadStopNestedBaseAPIResponse.setData(loadStopListBaseAPIResponses);

            return loadStopNestedBaseAPIResponse;
        }
        throw new ResourceNotFoundException();
    }
}
