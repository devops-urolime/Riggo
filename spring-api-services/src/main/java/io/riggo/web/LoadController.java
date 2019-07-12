package io.riggo.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import io.riggo.data.domain.Load;
import io.riggo.data.exception.LoadNotFoundException;
import io.riggo.data.exception.LoadObjectConfilictExeception;
import io.riggo.data.exception.RiggoDataAccessException;
import io.riggo.data.helpers.LoadAssembler;
import io.riggo.data.services.LoadImportService;
import io.riggo.data.services.LoadService;
import io.riggo.web.response.LoadResponse;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;


@RestController

@RequestMapping(value = Paths.API_VERSION)
public class LoadController extends BaseController {


    @Autowired
    private AutowireCapableBeanFactory beanFactory;


    private LoadImportService externalLoadService;// do not autowire this - set up at runtime only

    private ObjectMapper objectMapper;

    @Autowired
    private LoadService loadService;


    @GetMapping(value = Paths.LOAD + "/{id}")//., produces = "application/json")
    @ResponseBody
    @Cacheable(value = "loads", key = "#p0", unless = "#result == null")
    public LoadResponse getLoadById(@PathVariable("id") Long id, Authentication authentication) {
        // TODO: Remove this line in a future is just a demo authentication.
        //logger.debug(authentication.toString());
        Optional<Load> load = loadService.findById(id);
        if (load.isPresent())
            return new LoadResponse(load.get());
        else
            throw new LoadNotFoundException("Load not found");

    }

    /**
     * This methods looks for externalid even if that is an integer that is
     *
     * @param extSysId       external id
     * @param findByExternal flag
     * @return Load data
     */
    @Cacheable(value = "loadsEXT", key = "#p0")//for now
    @GetMapping("/load/external/{extSysId}")
    public Optional<Load> getLoadByExternalId(@PathVariable("extSysId") String extSysId, @RequestParam(required = false, name = "external", value = "false") Boolean findByExternal) {
        if (findByExternal) {
            return loadService.findByExtSysId(extSysId);
        } else {
            return loadService.findById(Long.valueOf(extSysId));
        }
    }


    /**
     * This method will map the salesforce payload, hopefully in an abstraction.
     * To CREATE a load and all other related entities
     */
    @PostMapping(path = "/load", produces = "application/json")
    public ResponseEntity addLoad(@RequestBody String json) throws LoadObjectConfilictExeception {
        return handleLoad(json, false);
    }


    /**
     * Process to update loads
     *
     * @param json data to be processed
     * @return response to the request
     * @throws LoadObjectConfilictExeception Conflict status
     * @throws RiggoDataAccessException      on data related failure
     */
    @PutMapping(value = "/load", produces = "application/json")
    @PatchMapping(value = "/load", produces = "application/json")

    public ResponseEntity updateLoad(@RequestBody final String json)
            throws LoadObjectConfilictExeception {


        return handleLoad(json, true);
    }


    /**
     * Get a json object for all requests when neded.
     *
     * @param json the json strng
     * @return a Map containing keys for the request.
     */

    private Map<String, Object> initJSON(String json) {
        LoadAssembler loadAssembler = new LoadAssembler(json);
        return loadAssembler.processProps();

    }

    /**
     * Comminicate with service to complete request
     *
     * @param json     The data
     * @param isUpdate Weather the request is an update or create
     * @return result of request
     * @throws LoadObjectConfilictExeception on creation attempt
     * @throws RiggoDataAccessException      on data realted failire
     */
    private ResponseEntity handleLoad(String json, boolean isUpdate) throws LoadObjectConfilictExeception {
        objectMapper = new ObjectMapper();

        String key;


        Map<String, Object> all = initJSON(json);

        key = (String) all.get("ext_sys_id");

        if (isUpdate && Strings.isNullOrEmpty(key))
            key = (String) all.get("mp_id"); // PUT and PATCH - can be by mp_id or ext_sys_id


        if (Strings.isNullOrEmpty(key)) {
            //RIG-154 comment by ed - return 404 if it can not found (11/7/2019)
            return new ResponseEntity<>(new JSONObject()
                    .put("message", "Not found.").toString(),
                    HttpStatus.NOT_FOUND);
        }

        externalLoadService = new LoadImportService();

        beanFactory.autowireBean(externalLoadService); // Wire On Demand.

        return externalLoadService.importLoad(logger, objectMapper, all, key, isUpdate);
    }


}
