package io.riggo.data.domain;

import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("MagicNumber")
public enum LoadStopArrivalStatus {
    EARLY(1, "Early"),
    DELAYED(2, "Late"),
    ;

    private final String displayName;
    private final int colVal;

    LoadStopArrivalStatus(int colVal, String displayName) {
        this.colVal = colVal;
        this.displayName = displayName;
    }

    public int getColVal() {
        return colVal;
    }

    public String getDisplayName() {
        return displayName;
    }

    private static final Map<Integer, LoadStopArrivalStatus> loadStopArrivalStatusMap;

    public static LoadStopArrivalStatus fromColVal(int colVal) {
        return loadStopArrivalStatusMap.get(colVal);
    }


    static {
        Map<Integer, LoadStopArrivalStatus> lsasMap = new HashMap<>();
        for (LoadStopArrivalStatus loadStopArrivalStatus : LoadStopArrivalStatus.values()) {
            lsasMap.put(loadStopArrivalStatus.getColVal(), loadStopArrivalStatus);
        }
        loadStopArrivalStatusMap = ImmutableMap.copyOf(lsasMap);
    }
}