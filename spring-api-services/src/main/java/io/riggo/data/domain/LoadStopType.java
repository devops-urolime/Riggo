package io.riggo.data.domain;

import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("MagicNumber")
public enum LoadStopType {
    FIRST_STOP(1, "First Stop"),
    STOP(2, "Stop"),
    LAST_STOP(2, "Last Stop"),
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

    public static LoadStopType fromColVal(int colVal) {
        return loadStopTypeMap.get(colVal);
    }

    static {
        Map<Integer, LoadStopType> lsMap = new HashMap<>();
        for (LoadStopType menuType : LoadStopType.values()) {
            lsMap.put(menuType.getColVal(), menuType);
        }
        loadStopTypeMap = ImmutableMap.copyOf(lsMap);
    }
}