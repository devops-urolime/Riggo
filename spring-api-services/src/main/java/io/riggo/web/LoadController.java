package io.riggo.web;

import java.util.Map;
import java.util.Optional;

import com.google.common.base.Strings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.riggo.data.domain.Load;
import io.riggo.data.exception.LoadObjectConfilictExeception;
import io.riggo.data.exception.RiggoDataAccessException;
import io.riggo.data.helpers.LoadAssembler;
import io.riggo.data.services.LoadImportService;
import io.riggo.data.services.LoadService;
import io.riggo.web.response.LoadResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@RestController

@RequestMapping(value = Paths.API_VERSION)
public class LoadController extends BaseController {


    LoadImportService externalLoadService;// do not autowire this - set up at runtime only
    Logger logger = LoggerFactory.getLogger(LoadController.class);
    ObjectMapper objectMapper;

    @Autowired
    private LoadService loadService;


    @GetMapping(value = Paths.LOAD + "/{id}")//., produces = "application/json")
    @ResponseBody
    @Cacheable(value = "loads", key = "#p0", unless = "#result == null")
    public LoadResponse getLoadById(@PathVariable("id") Long id, Authentication authentication) {
        // TODO: Remove this line in a future is just a demo authentication.
        logger.debug(authentication.toString());
        Optional<Load> load = loadService.findById(id);
        return new LoadResponse(load.get());
    }

    /**
     * This methods looks for externalid even if that is an integer that is
     *
     * @param extSysId
     * @param findByExternal
     * @return
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
    public ResponseEntity addLoad(@RequestHeader Map<String, String> headers, @RequestBody String json) throws LoadObjectConfilictExeception, RiggoDataAccessException {
        return handleLoad(headers, json, false);
    }


    /**
     *  Proces to update loads
     * @param headers reqest headers
     * @param json data to be processed
     * @return response to the request
     * @throws LoadObjectConfilictExeception
     * @throws RiggoDataAccessException
     */
    @PutMapping(value = "/load", produces = "application/json")
    @PatchMapping(value = "/load", produces = "application/json")
    public ResponseEntity updateLoad(@RequestHeader Map<String, String> headers, @RequestBody final String json) throws LoadObjectConfilictExeception, RiggoDataAccessException {
        return handleLoad(headers, json, true);
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
     * @param headers  The headers for access to token and other data for context awareness.
     * @param json     The data
     * @param isUpdate Weather the request is an update or create
     * @return result of request
     * @throws LoadObjectConfilictExeception
     * @throws RiggoDataAccessException
     */
    ResponseEntity handleLoad(@RequestHeader Map<String, String> headers, @RequestBody final String json, boolean isUpdate) throws LoadObjectConfilictExeception, RiggoDataAccessException {
        objectMapper = new ObjectMapper();
        ResponseEntity res = null;

        Map<String, Object> all = initJSON(json);
        String key = (String) all.get("ext_sys_id");
        if (Strings.isNullOrEmpty(key) && !isUpdate ) {
            //TODO: Why are we throwing a DataAccessException in a controller???
            //throw new RiggoDataAccessException("Load you are looking for needs an id");
        }

        externalLoadService = new LoadImportService();

        return externalLoadService.importLoad(logger, objectMapper, all, key, isUpdate);
    }


    public void validateToken(String token) {
        //String jtoken = "";
        try {
            DecodedJWT jwt = JWT.decode(token);
            Map claims = jwt.getClaims();

            String payload = jwt.getPayload();
            logger.error(claims.toString());
            // claims.containsKey("read")
        } catch (JWTDecodeException exception) {
            //Invalid token
        }
    }
}
