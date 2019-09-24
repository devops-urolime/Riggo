package io.riggo.data.domain;

import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("MagicNumber")
public enum LoadStopType {
    NO_TYPE(0, "No Type"),
    PICKUP(1, "Pickup"),
    DELIVERY(2, "Delivery"),
    ;

    private final String displayName;
    private final int colVal;

    LoadStopType(int colVal, String displayName) {
        this.colVal = colVal;
        this.displayName = displayName;
    }

    public int getColVal() {
        return colVal;
    }

    public String getDisplayName() {
        return displayName;
    }

    private static final Map<Integer, LoadStopType> loadStopTypeMap;
    private static final Map<String, LoadStopType> loadStopTypeDisplayNameMap;

    public static LoadStopType fromColVal(int colVal) {
        return loadStopTypeMap.get(colVal);
    }
    public static LoadStopType fromDisplayName(String displayName) {
        return loadStopTypeDisplayNameMap.get(displayName);
    }

    static {
        Map<Integer, LoadStopType> lsMap = new HashMap<>();
        Map<String, LoadStopType> lsdnMap = new HashMap<>();
        for (LoadStopType loadStopType : LoadStopType.values()) {
            lsMap.put(loadStopType.getColVal(), loadStopType);
            lsdnMap.put(loadStopType.getDisplayName(), loadStopType);
        }
        loadStopTypeMap = ImmutableMap.copyOf(lsMap);
        loadStopTypeDisplayNameMap = ImmutableMap.copyOf(lsdnMap);
    }
}