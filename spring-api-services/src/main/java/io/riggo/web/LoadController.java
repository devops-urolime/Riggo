package io.riggo.web;

import com.google.common.hash.Hashing;
import io.riggo.data.domain.*;
import io.riggo.data.exception.BadRequestException;
import io.riggo.data.exception.ResourceAlreadyExistsException;
import io.riggo.data.exception.ResourceNotFoundException;
import io.riggo.data.services.*;
import io.riggo.web.integration.SalesforceRevenovaConstants;
import io.riggo.web.integration.parser.SalesforceRevenovaRequestBodyParserPostPutLoad;
import io.riggo.web.response.LoadAPIResponse;
import io.riggo.web.response.LoadResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @Autowired
    private LocationService locationService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private SalesforceRevenovaRequestBodyParserPostPutLoad salesforceRevenovaRequestBodyParserPostPutLoad;

    @Autowired
    private AuthenticationFacade authenticationFacade;


    @GetMapping(value = Paths.LOAD + "/{id}")//., produces = "application/json")
    @ResponseBody
    @PreAuthorize("hasAuthority('read:load')")
    //@Cacheable(value = "loads", key = "#p0", unless = "#result == null")
    public LoadResponse getLoadById(@PathVariable("id") String id) throws ResourceNotFoundException, BadRequestException{
        if(NumberUtils.isDigits(id)){
            Optional<Load> load = loadService.findById(NumberUtils.toInt(id), authenticationFacade.getSiteId());
            if(load.isPresent()) {
                return new LoadResponse(load.get());
            }
            throw new ResourceNotFoundException(ResourceType.LOAD, id);
        }
        throw new BadRequestException();
    }


    @Cacheable(value = "loadsEXT", key = "#p0")//for now
    @GetMapping("/load/external/{extSysId}")
    @PreAuthorize("hasAuthority('read:load')")
    public Optional<Load> getLoadByExternalId(@PathVariable("extSysId") String extSysId, @RequestParam(required = false, name = "external", value = "false") Boolean findByExternal, Authentication authentication) {
        if (findByExternal) {
            return loadService.findByExtSysId(extSysId, authenticationFacade.getSiteId());
        }
        return loadService.findById(Integer.valueOf(extSysId), authenticationFacade.getSiteId());
    }


    @PostMapping(path = Paths.LOAD, produces = "application/json")
    @PreAuthorize("hasAuthority('write:load')")
    public LoadAPIResponse postLoad(@RequestBody Map<String, Object> dataHashMap) throws ResourceAlreadyExistsException {
        //TODO: resolve parsers based on site and integration
        Load load = salesforceRevenovaRequestBodyParserPostPutLoad.resolveLoad(dataHashMap);
        load.setSiteId(authenticationFacade.getSiteId());

        Optional<Load> checkLoadExists = loadService.findByExtSysId(load.getExtSysId(), authenticationFacade.getSiteId());
        if (checkLoadExists.isPresent()) {
            throw new ResourceAlreadyExistsException(ResourceType.LOAD, checkLoadExists.get().getId());
        }

        Shipper shipper = salesforceRevenovaRequestBodyParserPostPutLoad.resolveShipper(dataHashMap, authenticationFacade.getSiteId());
        persistShipper(shipper, load, authenticationFacade.getSiteId());

        EquipmentType equipmentType = salesforceRevenovaRequestBodyParserPostPutLoad.resolveEquipmentType(dataHashMap);
        if (equipmentType != null && StringUtils.isNotBlank(equipmentType.getExtSysId())) {
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

        persistLoadStop(firstStop, load.getId());
        persistLoadStop(lastStop, load.getId());

        LoadAPIResponse loadAPIResponse = new LoadAPIResponse();
        loadAPIResponse.addData(load);
        loadAPIResponse.setHashId(Hashing.sha256()
                .hashString(load.getExtSysId() + load.getId(), StandardCharsets.UTF_8)
                .toString());

        return loadAPIResponse;
    }


    @PutMapping(value = Paths.LOAD, produces = "application/json")
    @PreAuthorize("hasAuthority('write:load')")
    public LoadAPIResponse updateLoad(@RequestBody Map<String, Object> dataHashMap) throws ResourceNotFoundException{
        Load resolvedLoad = salesforceRevenovaRequestBodyParserPostPutLoad.resolveLoad(dataHashMap);
        Optional<Load> loadFromDb = loadService.findByExtSysId(resolvedLoad.getExtSysId(), authenticationFacade.getSiteId());
        if (!loadFromDb.isPresent()) {
            throw new ResourceNotFoundException(ResourceType.LOAD, resolvedLoad.getExtSysId());
        }
        resolvedLoad.setSiteId(authenticationFacade.getSiteId());

        Shipper shipper = salesforceRevenovaRequestBodyParserPostPutLoad.resolveShipper(dataHashMap, authenticationFacade.getSiteId());
        persistShipper(shipper, resolvedLoad, authenticationFacade.getSiteId());

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

    private void persistShipper(Shipper shipper, Load load, Integer siteId) {
        if (StringUtils.isNotBlank(shipper.getExtSysId())) {
            Optional<Shipper> checkShipperExists = shipperService.findByExtSysId(shipper.getExtSysId(), siteId);
            if (checkShipperExists.isPresent()) {
                BeanUtils.copyProperties(checkShipperExists.get(), shipper, SalesforceRevenovaConstants.POST_PUT_SHIPPER_IGNORE_PROPERTIES);
            }
            shipperService.save(shipper);
            load.setShipperId(shipper.getId());
        }
    }

    private void persistLoadStop(LoadStop loadStop, Integer loadId)
    {
        Location location = loadStop.getLocation();
        if(location != null) {
            Optional<Location> checkLocationExists = null;
            Optional<Address> checkAddressExists = null;
            if (StringUtils.isNotBlank(location.getExtSysId())) {
                checkLocationExists = locationService.findByExtSysId(location.getExtSysId());
                if (checkLocationExists.isPresent() && checkLocationExists.get().getAddressId() != null) {
                    checkAddressExists = addressService.findById(checkLocationExists.get().getAddressId());
                }
            }

            Address address = location.getAddress();
            if(address != null) {
                if (checkAddressExists != null && checkAddressExists.isPresent()) {
                    BeanUtils.copyProperties(checkAddressExists.get(), address, SalesforceRevenovaConstants.POST_PUT_LOAD_STOP_LOCATION_ADDRESS_IGNORE_PROPERTIES);
                }
                addressService.save(address);
                location.setAddressId(address.getId());
            }

            if (checkLocationExists != null && checkLocationExists.isPresent()) {
                BeanUtils.copyProperties(checkLocationExists.get(), location, SalesforceRevenovaConstants.POST_PUT_LOAD_STOP_LOCATION_IGNORE_PROPERTIES);
            }
            locationService.save(location);
            loadStop.setLocationId(location.getId());
        }

        if (StringUtils.isNotBlank(loadStop.getExtSysId())) {
            Optional<LoadStop> checkFirstStopExists = loadStopService.findByExtSysId(loadStop.getExtSysId(), authenticationFacade.getSiteId());
            if (!checkFirstStopExists.isPresent()) {
                loadStop.setLoadId(loadId);
                loadStopService.save(loadStop);
            }
        }
    }
}
