package io.riggo.data.helpers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import org.springframework.context.annotation.PropertySource;

import java.util.Map;

@PropertySource(value = "classpath:io/riggo/data/helpers/loadmapping.properties")
public class LoadAssembler extends RiggoAssembler {


    public LoadAssembler(String loadJSON) {

        super(loadJSON, "io/riggo/data/helpers/loadmapping.properties");

    }

    @Override
    /**
     *Process JSON based on know paths
     @return Map of Key and Value mapped by object mapper
     */
    public Map<String, Object> processProps() {
        ObjectMapper om = new ObjectMapper();
        allProps.forEach((k, v) -> {
            String vVal = (String) v;

            Object tmp;
            try {
                tmp = JsonPath.read(getJson(), vVal);
                if (tmp != null) {


                    String s = om.writeValueAsString(tmp);
                    allVals.put(k, s);

                } else
                    allVals.put(k, "");
            } catch (JsonProcessingException e) {
                allVals.put(k, "");
            } catch (PathNotFoundException e) {
                allVals.put(k, "");
            }
        });

        return allVals;

    }

}

