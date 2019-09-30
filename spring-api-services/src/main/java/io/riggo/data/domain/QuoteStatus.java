package io.riggo.data.domain;

import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings( "MagicNumber" )
public enum QuoteStatus
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

    QuoteStatus(int colVal, String displayName )
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

    private static final Map<Integer, QuoteStatus> quoteStatusMap;
    private static final Map<String, QuoteStatus> quoteStatusDisplayNameMap;

    public static QuoteStatus fromColVal(int colVal )
    {
        return quoteStatusMap.get( colVal );
    }

    public static QuoteStatus fromDisplayName(String displayName )
    {
        return quoteStatusDisplayNameMap.get( displayName );
    }

    static
    {
        Map<Integer, QuoteStatus> qsMap = new HashMap<>();
        Map<String, QuoteStatus> qsDnMap = new HashMap<>();
        for( QuoteStatus quoteStatus : QuoteStatus.values() )
        {
            qsMap.put( quoteStatus.getColVal(), quoteStatus);
            qsDnMap.put( quoteStatus.getDisplayName(), quoteStatus);
        }
        quoteStatusMap = ImmutableMap.copyOf(qsMap);
        quoteStatusDisplayNameMap = ImmutableMap.copyOf(qsDnMap);
    }
}