package io.riggo.data.helpers;


import com.jayway.jsonpath.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


public abstract class RiggoAssembler {
    @Autowired
    Environment env;


    Map<String, Object> allProps = new HashMap();

    Map<String, Object> allVals = new HashMap();


    private Object json = null;

    private String payload;


    public RiggoAssembler(String json, String propfile) {
        this.setPayload(json);

        try {
            setJson(Configuration.defaultConfiguration().jsonProvider().parse(json));

        } catch (Exception e) {
            throw e;
        }


        Map<String, String> someMap = new HashMap<>();
        try {
            Properties props = PropertiesLoaderUtils.loadAllProperties(propfile);
            for (Object key : props.keySet()) {
                allProps.put(key.toString(), props.getProperty(key.toString()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public Object getJson() {
        return json;
    }

    public void setJson(Object json) {
        this.json = json;
    }


    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public abstract Map<String, Object> processProps();


}
