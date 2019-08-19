package io.riggo.data.domain;

import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings( "MagicNumber" )
public enum Role
{
    /** Internal Super-Admin **/
    SUPER_ADMIN( 100, "Site level super admin", "super-admin" ),

    /** Site Administrator **/
    SITE_ADMIN( 80, "Site Administrator", "site-admin"),

    /** Shipper Executive */
    SHIPPER_EXECUTIVE( 59, "Shipper Executive", "shipper-exec"),
    SHIPPER_OPERATOR( 55, "Shipper Operator", "shipper-operator" ),

    /** Carrier Executive */
    CARRIER_EXECUTIVE( 39, "Carrier Executive", "carrier-exec"),
    CARRIER_OPERATOR( 35, "Carrier Operator", "carrier-operator" ),
    CARRIER_DRIVER( 32, "Carrier Driver", "carrier-driver" )
    ;


    private final int colVal;
    private final String displayName;
    private final String auth0Role;


    Role(int colVal, String displayName, String auth0Role )
    {
        this.colVal = colVal;
        this.displayName = displayName;
        this.auth0Role = auth0Role;
    }

    public int getColVal()
    {
        return colVal;
    }

    public String getDisplayName()
    {
        return displayName;
    }

    public String getAuth0Role(){
        return auth0Role;
    }

    private static final Map<Integer, Role> roleColValMap;
    private static final Map<String, Role> roleAuth0Map;

    public static Role fromColVal(int colVal )
    {
        return roleColValMap.get( colVal );
    }

    public static Role fromAuth0(String auth0Role )
    {
        return roleAuth0Map.get( auth0Role );
    }

    static
    {
        Map<Integer, Role> roleMap = new HashMap<>();
        Map<String, Role> authMap = new HashMap<>();

        for( Role role : Role.values() )
        {
            roleMap.put( role.getColVal(), role );
            authMap.put( role.getAuth0Role(), role );
        }
        roleColValMap = ImmutableMap.copyOf(roleMap);
        roleAuth0Map = ImmutableMap.copyOf(authMap);
    }
}