package io.riggo.data.domain;

import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("MagicNumber")
public enum LoadStopStatus {
    NO_STATUS(0, "No Status"),
    EN_ROUTE(1, "En Route"),
    ARRIVED(2, "Arrived"),
    DEPARTED(3, "Departed"),
    ;

    private final String displayName;
    private final int colVal;

    LoadStopStatus(int colVal, String displayName) {
        this.colVal = colVal;
        this.displayName = displayName;
    }

    public int getColVal() {
        return colVal;
    }

    public String getDisplayName() {
        return displayName;
    }

    private static final Map<Integer, LoadStopStatus> loadStopStatusMap;
    private static final Map<String, LoadStopStatus> loadStopStatusDisplayNameMap;

    public static LoadStopStatus fromColVal(int colVal) {
        return loadStopStatusMap.get(colVal);
    }
    public static LoadStopStatus fromDisplayName(String displayName) {
        return loadStopStatusDisplayNameMap.get(displayName);
    }

    static {
        Map<Integer, LoadStopStatus> lsMap = new HashMap<>();
        Map<String, LoadStopStatus> lsdnMap = new HashMap<>();
        for (LoadStopStatus loadStopStatus : LoadStopStatus.values()) {
            lsMap.put(loadStopStatus.getColVal(), loadStopStatus);
            lsdnMap.put(loadStopStatus.getDisplayName(), loadStopStatus);
        }
        loadStopStatusMap = ImmutableMap.copyOf(lsMap);
        loadStopStatusDisplayNameMap = ImmutableMap.copyOf(lsdnMap);
    }
}