package io.riggo.data.domain;

import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("MagicNumber")
public enum LoadStopCarrierStatus {
    NO_ISSUES(1, "No Issues", LoadStopStatus.EN_ROUTE),
    DELAYED_BY_WEATHER(2, "Delayed By Weather", LoadStopStatus.EN_ROUTE),
    DELAYED_BY_TRAFFIC(3, "Delayed By Traffic", LoadStopStatus.EN_ROUTE),
    DELAYED_BY_EQUIPMENT_BREAKDOWN(4, "Delayed By Equipment Breakdown", LoadStopStatus.EN_ROUTE),
    EN_ROUTE_OTHER(5, "Other (see comments)", LoadStopStatus.EN_ROUTE),
    CHECKED_IN(10, "Checked In", LoadStopStatus.ARRIVED),
    LOADING(11, "Loading", LoadStopStatus.ARRIVED),
    UNLOADING(12, "Unloading", LoadStopStatus.ARRIVED),
    IN_DETENTION(13, "In Detention", LoadStopStatus.ARRIVED),
    ARRIVED_OTHER(14, "Other (see comments)", LoadStopStatus.ARRIVED),
    LOADED_CLEAN(20, "Loaded Clean", LoadStopStatus.DEPARTED),
    LOADED_WITH_ISSUES(21, "Loaded with Issues", LoadStopStatus.DEPARTED ),
    UNLOADED_CLEAN(22, "Unloaded Clean", LoadStopStatus.DEPARTED ),
    UNLOADED_WITH_ISSUES(23, "Unloaded with Issues", LoadStopStatus.DEPARTED ),
    DEPARTED_OTHER(24, "Other (see comments)", LoadStopStatus.DEPARTED),
    ;

    private final String displayName;
    private final int colVal;
    private final LoadStopStatus loadStopStatus;

    LoadStopCarrierStatus(int colVal, String displayName, LoadStopStatus loadStopStatus) {
        this.colVal = colVal;
        this.displayName = displayName;
        this.loadStopStatus = loadStopStatus;
    }

    public int getColVal() {
        return colVal;
    }

    public String getDisplayName() {
        return displayName;
    }

    private static final Map<Integer, LoadStopCarrierStatus> loadStopCarrierStatusMap;
    private static final Map<String, LoadStopCarrierStatus> loadStopCarrierStatusDisplayNameMap;

    public static LoadStopCarrierStatus fromColVal(int colVal) {
        return loadStopCarrierStatusMap.get(colVal);
    }
    public static LoadStopCarrierStatus fromDisplayName(String displayName) {
        return loadStopCarrierStatusDisplayNameMap.get(displayName);
    }

    static {
        Map<Integer, LoadStopCarrierStatus> lscsMap = new HashMap<>();
        Map<String, LoadStopCarrierStatus> lscsdnMap = new HashMap<>();
        for (LoadStopCarrierStatus loadStopCarrierStatus : LoadStopCarrierStatus.values()) {
            lscsMap.put(loadStopCarrierStatus.getColVal(), loadStopCarrierStatus);
            lscsdnMap.put(loadStopCarrierStatus.getDisplayName(), loadStopCarrierStatus);
        }
        loadStopCarrierStatusMap = ImmutableMap.copyOf(lscsMap);
        loadStopCarrierStatusDisplayNameMap = ImmutableMap.copyOf(lscsdnMap);
    }
}