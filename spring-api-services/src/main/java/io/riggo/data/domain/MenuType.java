package io.riggo.data.domain;

import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings( "MagicNumber" )
public enum MenuType
{
    LEFT_HAND_MENU( 1, "Left Hand Menu" );

    private final String displayName;
    private final int colVal;

    MenuType(int colVal, String displayName )
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

    private static final Map<Integer, MenuType> menuTypeMap;

    public static MenuType fromColVal(int colVal )
    {
        return menuTypeMap.get( colVal );
    }

    static
    {
        Map<Integer, MenuType> mtMap = new HashMap<>();
        for( MenuType menuType : MenuType.values() )
        {
            mtMap.put( menuType.getColVal(), menuType );
        }
        menuTypeMap = ImmutableMap.copyOf(mtMap);
    }
}