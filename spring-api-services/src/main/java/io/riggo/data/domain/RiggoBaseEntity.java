package io.riggo.data.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.hash.Hashing;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.nio.charset.StandardCharsets;


public class RiggoBaseEntity {

    @JsonIgnore
    @Transient
    private static String source = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    @JsonIgnore
    @Transient
    private static String target = "@5A8ZWS0XEDC6RFVT9GBY4HNU3J2MI1KO7L~";
    @JsonIgnore
    @Transient
    private final String DELIMITER = ".";


    @JsonIgnore
    @Transient
    public final Integer FORWARD = 0;

    @JsonIgnore
    @Transient
    public final Integer REVERSE = 1;

    @JsonIgnore
    @Column(name = "hash")
    private String hash;

    @JsonIgnore
    @Transient
    private String prefix = "tbl";

    public String getPrefix() {
        return prefix;
    }

    void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getHash() {
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
            e.printStackTrace();
        }
        return "";
    }


    /**
     * Dictionary based decode
     * This allows for decoding without storing.
     *
     * @param s soruce for encoding
     * @param direction direction of encoding
     * @return encoded string
     */

    public String encode(String s, int direction) {


        String src, tgt;
        String workableValue = s;

        if (direction == FORWARD) {
            //encode
            src = source;
            tgt = target;
        } else {
            //decode
            if (workableValue.contains(DELIMITER)) {
                workableValue = workableValue.substring(workableValue.lastIndexOf(DELIMITER) + 1);
            }
            src = target;
            tgt = source;
        }
        char[] result = new char[workableValue.length()];
        for (int i = 0; i < workableValue.length(); i++) {
            char c = workableValue.charAt(i);
            int index = src.indexOf(c);
            result[i] = tgt.charAt(index);
        }
        String sha = "";
        if (direction == 0)
            sha = "ey" + getHashCode(prefix) + DELIMITER;// add - ignore to the left of this ;

        if (direction == REVERSE)
            return new String(result);
        else
            return sha + new String(result);
    }

    String getHashCode(String value) {

        return Hashing.sha256()
                .hashString(prefix + value, StandardCharsets.UTF_8)
                .toString();

    }

    public String getHashAsJsonString(String id) {
        return getHashCode(prefix) + encode(id, 0);


    }
}
