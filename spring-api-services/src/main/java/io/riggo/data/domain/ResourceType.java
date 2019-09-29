package io.riggo.data.domain;

import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings( "MagicNumber" )
public enum ResourceType
{
    NORESOURCE( 0, "No Resource" ),
    LOAD( 1, "Load" ),
    SHIPPER(2, "Shipper"),
    LOAD_PIPELINE(3, "Load Pipeline"),
    LOAD_STOP_SUMMARY(4, "Load Stop Summary"),
    QUOTE(5, "Quote"),
    SHIPMENT_SUMMARY(6, "Shipment Summary"),
    ;

    private final String displayName;
    private final int colVal;

    ResourceType( int colVal, String displayName )
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


    private static final Map<Integer, ResourceType> resourceTypeMap;

    public static ResourceType fromColVal( int colVal )
    {
        return resourceTypeMap.get( colVal );
    }

    static
    {
        Map<Integer, ResourceType> rtMap = new HashMap<>();
        for( ResourceType resourceType : ResourceType.values() )
        {
            rtMap.put( resourceType.getColVal(), resourceType );
        }
        resourceTypeMap = ImmutableMap.copyOf(rtMap);
    }
}