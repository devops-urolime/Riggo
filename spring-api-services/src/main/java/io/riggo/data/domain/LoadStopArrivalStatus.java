package io.riggo.data.domain;

import com.google.common.collect.ImmutableMap;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("MagicNumber")
public enum LoadStopArrivalStatus {
    NO_STATUS(0, "No Status"),
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
    private static final Map<String, LoadStopArrivalStatus> loadStopArrivalStatusDisplayNameMap;

    public static LoadStopArrivalStatus fromColVal(int colVal) {
        return loadStopArrivalStatusMap.get(colVal);
    }

    public static LoadStopArrivalStatus fromDisplayName(String displayName) {
        return loadStopArrivalStatusDisplayNameMap.get(displayName);
    }



    static {
        Map<Integer, LoadStopArrivalStatus> lsasMap = new HashMap<>();
        Map<String, LoadStopArrivalStatus> lsasdnMap = new HashMap<>();
        Arrays.stream(LoadStopArrivalStatus.values()).forEach(loadStopArrivalStatus -> {
            lsasMap.put(loadStopArrivalStatus.getColVal(), loadStopArrivalStatus);
            lsasdnMap.put(loadStopArrivalStatus.getDisplayName(), loadStopArrivalStatus);
        });
        loadStopArrivalStatusMap = ImmutableMap.copyOf(lsasMap);
        loadStopArrivalStatusDisplayNameMap = ImmutableMap.copyOf(lsasdnMap);
    }
}