package io.riggo.web;

import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings( "MagicNumber" )
public enum ShipmentVizPeriod
{
    YEARS(0, "Years"),
    QUARTERS( 1, "Quarters"),
    MONTHS( 2, "Months"),
    WEEKS( 3, "Weeks"),
    DAYS( 4, "Days")
    ;


    private final String displayName;
    private final int colVal;

    ShipmentVizPeriod(int colVal, String displayName )
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

    private static final Map<Integer, ShipmentVizPeriod> shipmentVizPeriodMap;

    public static ShipmentVizPeriod fromColVal(int colVal )
    {
        return shipmentVizPeriodMap.get( colVal );
    }


    static
    {
        Map<Integer, ShipmentVizPeriod> svMap = new HashMap<>();
        for( ShipmentVizPeriod shipmentVizPeriod : ShipmentVizPeriod.values() )
        {
            svMap.put( shipmentVizPeriod.getColVal(), shipmentVizPeriod );

        }
        shipmentVizPeriodMap = ImmutableMap.copyOf(svMap);
    }
}