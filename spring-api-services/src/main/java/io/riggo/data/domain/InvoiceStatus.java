package io.riggo.data.domain;

import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings( "MagicNumber" )
public enum InvoiceStatus
{
    NO_STATUS( 0, "None"),
    PENDING( 1, "Pending"),
    QUOTED( 2, "Quoted"),
    ACCEPTED( 3, "Accepted"),
    REJECTED( 4, "Rejected"),
    CANCELLED( 5, "Cancelled"),
    EXPIRED( 6, "Expired"),
    ;


    private final String displayName;
    private final int colVal;

    InvoiceStatus(int colVal, String displayName )
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

    private static final Map<Integer, InvoiceStatus> invoiceStatusMap;
    private static final Map<String, InvoiceStatus> invoiceStatusDisplayNameMap;

    public static InvoiceStatus fromColVal(int colVal )
    {
        return invoiceStatusMap.get( colVal );
    }

    public static InvoiceStatus fromDisplayName( String displayName )
    {
        return invoiceStatusDisplayNameMap.get( displayName );
    }

    static
    {
        Map<Integer, InvoiceStatus> isMap = new HashMap<>();
        Map<String, InvoiceStatus> isDnMap = new HashMap<>();
        for( InvoiceStatus invoiceStatus : InvoiceStatus.values() )
        {
            isMap.put( invoiceStatus.getColVal(), invoiceStatus );
            isDnMap.put( invoiceStatus.getDisplayName(), invoiceStatus );
        }
        invoiceStatusMap = ImmutableMap.copyOf(isMap);
        invoiceStatusDisplayNameMap = ImmutableMap.copyOf(isDnMap);
    }
}