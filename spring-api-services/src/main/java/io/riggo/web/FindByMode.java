package io.riggo.web;

import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings( "MagicNumber" )
public enum FindByMode
{
    INTERNAL( 0, "Internal" ),
    EXTERNAL( 1, "External" ),
    ;

    private final String displayName;
    private final int colVal;

    FindByMode(int colVal, String displayName )
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


    private static final Map<Integer, FindByMode> findByMap;

    public static FindByMode fromColVal(int colVal )
    {
        return findByMap.get( colVal );
    }

    static
    {
        Map<Integer, FindByMode> fbMap = new HashMap<>();
        for( FindByMode findByMode : FindByMode.values() )
        {
            fbMap.put( findByMode.getColVal(), findByMode );
        }
        findByMap = ImmutableMap.copyOf(fbMap);
    }
}