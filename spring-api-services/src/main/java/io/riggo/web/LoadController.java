package io.riggo.web;

import com.google.common.hash.Hashing;
import io.riggo.data.domain.*;
import io.riggo.data.exception.ResourceAlreadyExistsException;
import io.riggo.data.services.EquipmentTypeService;
import io.riggo.data.services.LoadService;
import io.riggo.data.services.LoadStopService;
import io.riggo.data.services.ShipperService;
import io.riggo.web.integration.parser.SalesforceRevenovaRequestBodyParser;
import io.riggo.web.response.LoadAPIResponse;
import io.riggo.web.response.LoadResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;


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
    public LoadAPIResponse postLoad(@RequestHeader Map<String, String> headers, @RequestBody Map<String, Object> dataHashMap) {
        //TODO: resolve parsers based on site and integration
        SalesforceRevenovaRequestBodyParser salesforceRevenovaRequestBodyParser = new SalesforceRevenovaRequestBodyParser(dataHashMap);
        Load load = salesforceRevenovaRequestBodyParser.resolveLoad();

        Optional<Load> checkLoadExists = loadService.findByExtSysId(load.getExtSysId());
        if (checkLoadExists.isPresent()) {
            throw new ResourceAlreadyExistsException(ResourceType.LOAD, checkLoadExists.get().getId());
        }

        Shipper shipper = salesforceRevenovaRequestBodyParser.resolveShipper();
        if (StringUtils.isNotBlank(shipper.getExtSysId())) {
            Optional<Shipper> checkShipperExists = shipperService.findByExtSysId(shipper.getExtSysId());
            if (checkShipperExists.isPresent()) {
                //TODO:Update needs to copy non-null values
            }
            shipperService.save(shipper);
        }
        load.setShipperId(shipper.getId());

        EquipmentType equipmentType = salesforceRevenovaRequestBodyParser.resolveEquipmentType();
        if (StringUtils.isNotBlank(equipmentType.getExtSysId())) {
            Optional<EquipmentType> checkEquipmentTypeExists = equipmentTypeService.findByExtSysId(equipmentType.getExtSysId());
            if (checkEquipmentTypeExists.isPresent()) {
                equipmentTypeService.save(equipmentType);
            }
        }
        load.setEquipmentTypeId(equipmentType.getId());

        LoadStop firstStop = salesforceRevenovaRequestBodyParser.resolveFirstStop();
        LoadStop lastStop = salesforceRevenovaRequestBodyParser.resolveLastStop();

        if (StringUtils.isNotBlank(firstStop.getExtSysId())) {
            Optional<LoadStop> checkFirstStopExists = loadStopService.findByExtSysId(firstStop.getExtSysId());
            if (!checkFirstStopExists.isPresent()) {
                loadStopService.save(firstStop);
            }
        }


        if (StringUtils.isNotBlank(lastStop.getExtSysId())) {
            Optional<LoadStop> checkLastStopExists = loadStopService.findByExtSysId(lastStop.getExtSysId());
            if (checkLastStopExists.isPresent()) {
                loadStopService.save(lastStop);
            }
        }
        load = loadService.save(load);

        LoadAPIResponse loadAPIResponse = new LoadAPIResponse();
        loadAPIResponse.addData(load);
        loadAPIResponse.setHashId(Hashing.sha256()
                .hashString(load.getExtSysId() + load.getId(), StandardCharsets.UTF_8)
                .toString());

        return loadAPIResponse;
    }


    @PutMapping(value = "/load", produces = "application/json")
    @PatchMapping(value = "/load", produces = "application/json")
    public ResponseEntity updateLoad(@RequestHeader Map<String, String> headers, @RequestBody String json) {
        return null;
    }
}
