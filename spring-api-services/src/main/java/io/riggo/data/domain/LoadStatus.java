package io.riggo.data.domain;

import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.Map;

public enum LoadStatus
{
    NO_STATUS( 0, "No Status" ),
    PENDING( 1, "Pending" ),
    IN_TRANSIT(2, "In Transit"),
    DELIVERED( 3, "Delivered"),
    ;

    private final String displayName;
    private final int colVal;

    LoadStatus(int colVal, String displayName )
    {
        this.colVal = colVal;
        this.displayName = displayName;
    }

    public int getColVal()
    {
        return colVal;
    }

    public String getDisplayName()
    {
        return displayName;
    }

    private static final Map<Integer, LoadStatus> loadStatusMap;

    public static LoadStatus fromColVal(int colVal )
    {
        return loadStatusMap.get( colVal );
    }

    static
    {
        Map<Integer, LoadStatus> lsMap = new HashMap<>();
        for( LoadStatus loadStatus : LoadStatus.values() )
        {
            lsMap.put( loadStatus.getColVal(), loadStatus );
        }
        loadStatusMap = ImmutableMap.copyOf(lsMap);
    }
}