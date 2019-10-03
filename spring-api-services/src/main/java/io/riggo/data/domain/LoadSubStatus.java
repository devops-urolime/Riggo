package io.riggo.data.domain;

import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings( "MagicNumber" )
public enum LoadSubStatus
{
    NO_STATUS( 0, "No Status", LoadStatus.NO_STATUS ),
    QUOTED( 1, "Quoted", LoadStatus.PENDING ),
    BOOKED( 2, "Booked", LoadStatus.PENDING ),
    DISPATCHED( 3, "Dispatched", LoadStatus.IN_TRANSIT ),
    AT_PICKUP( 4, "At Pickup", LoadStatus.IN_TRANSIT ),
    IN_TRANSIT( 5, "In transit", LoadStatus.IN_TRANSIT ),
    AT_DELIVERY( 6, "At Delivery", LoadStatus.IN_TRANSIT ),
    PENDING_DOCUMENTS( 7, "Pending Docs", LoadStatus.DELIVERED ),
    DOCUMENTS_RECEIVED( 8, "Docs Received", LoadStatus.DELIVERED ),
    INVOICED( 9, "Invoiced", LoadStatus.DELIVERED ),
    ;


    private final String displayName;
    private final int colVal;
    private LoadStatus loadStatus;

    LoadSubStatus(int colVal, String displayName, LoadStatus loadStatus )
    {
        this.colVal = colVal;
        this.displayName = displayName;
        this.loadStatus = loadStatus;
    }

    public int getColVal()
    {
        return colVal;
    }

    public String getDisplayName()
    {
        return displayName;
    }

    public LoadStatus getLoadStatus() {
        return loadStatus;
    }

    private static final Map<Integer, LoadSubStatus> loadSubStatusMap;

    public static LoadSubStatus fromColVal(int colVal )
    {
        return loadSubStatusMap.get( colVal );
    }

    static
    {
        Map<Integer, LoadSubStatus> lssMap = new HashMap<>();
        for( LoadSubStatus loadSubStatus : LoadSubStatus.values() )
        {
            lssMap.put( loadSubStatus.getColVal(), loadSubStatus );
        }
        loadSubStatusMap = ImmutableMap.copyOf(lssMap);
    }
}