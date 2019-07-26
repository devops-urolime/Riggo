package io.riggo.web.integration.parser;

import io.riggo.web.integration.exception.PayloadParseException;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.util.NumberUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Component
@Scope(value="prototype", proxyMode= ScopedProxyMode.TARGET_CLASS)
public class SalesforceRevenovaRequestBodyParserHelper {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    public String getMapValueAsString(String key, Map<String, Object> map) {
        String val = MapUtils.getString(map, key);
        return StringUtils.equals(val, "null") ? null : val;
    }


    public Integer getMapValueAsInteger(String key, Map<String, Object> map) {
        return MapUtils.getInteger(map, key);
    }


    public BigDecimal getMapValueAsBigDecimal(String key, Map<String, Object> map) {
        String value = (String) map.get(key);
        if (value == null) {
            return null;
        }
        if (StringUtils.isNotBlank(value)) {
            try {
                return NumberUtils.parseNumber(value, BigDecimal.class);
            } catch (IllegalArgumentException iae) {
                throw new PayloadParseException(key, iae);
            }
        }
        throw new PayloadParseException(key);
    }


    public Boolean getMapValueAsBoolean(String key, Map<String, Object> map) {
        return MapUtils.getBoolean(map, key);
    }


    public Map<String, Object> getMapValueAsMap(String key, Map<String, Object> map) {
        return (Map<String, Object>) MapUtils.getMap(map, key);
    }


    public LocalDateTime getMapValueAsLocalDateTime(String key, Map<String, Object> map) {
        String value = (String) map.get(key);
        if (StringUtils.isBlank(value)) {
            return null;
        }
        return LocalDateTime.parse(value, dateTimeFormatter);
    }


    public LocalDateTime getMapValueAsLocalDateTimeFrom2Keys(String dateKey, String timeKey, Map<String, Object> map) {
        String dateValue = (String) map.get(dateKey);
        String timeValue = (String) map.get(timeKey);
        if (StringUtils.isBlank(dateValue)) {
            return null;
        }
        if(StringUtils.isBlank(timeValue))
        {
            timeValue = "00:00:00";
        }
        return LocalDateTime.parse(dateValue + " " + timeValue, dateTimeFormatter);
    }


    public LocalDate getMapValueAsLocalDate(String key, Map<String, Object> map) {
        String value = (String) map.get(key);
        if (StringUtils.isNotBlank(key)) {
            return null;
        }
        return LocalDate.parse(value, dateFormatter);
    }
}
