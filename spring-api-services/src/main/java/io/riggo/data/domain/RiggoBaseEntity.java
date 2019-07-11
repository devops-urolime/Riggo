package io.riggo.data.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.hash.Hashing;

import org.json.JSONObject;

import javax.persistence.Column;

import javax.persistence.Transient;
import java.nio.charset.StandardCharsets;


public class RiggoBaseEntity {

    static String source = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    static String target = "@5A8ZWS0XEDC6RFVT9GBY4HNU3J2MI1KO7L~";


    @Column(name = "hash")
    private String hash;

    @Transient
    String prefix = "tbl";

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getHash(String s) {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String toJson() {

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);

        try {
            return mapper.writerWithDefaultPrettyPrinter()
                    .writeValueAsString(this);
        } catch (Exception e) {

        }
        return "";
    }


    /**
     * dictionary based decode
     * This allows for decoding without storing.
     *
     * @param s
     * @param direction
     * @return
     */

    public String encode(String s, int direction) {


        String src, tgt;
        String workableValue = s;

        if (direction == 0) {
            //encode
            src = source;
            tgt = target;
        } else {
            //decode
            if (workableValue.contains("-")) {
                workableValue = workableValue.substring(workableValue.indexOf("-") - 1);
            }
            src = target;
            tgt = source;
        }
        char[] result = new char[10];
        for (int i = 0; i < s.length(); i++) {
            char c = workableValue.charAt(i);
            int index = src.indexOf(c);
            result[i] = tgt.charAt(index);
        }
        String sha = "";
        if (direction == 0)
            sha = getHashCode(prefix) + "-";// add - ignore to the left of this ;

        return sha + result;
    }

    public String getHashCode(String value) {

        return Hashing.sha256()
                .hashString(prefix + value, StandardCharsets.UTF_8)
                .toString();

    }

    public String getHashAsJsonString(String id) {
        return new JSONObject()
                .put("mpid", getHashCode(prefix + id))
                .toString();

    }
}
