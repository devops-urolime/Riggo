package io.riggo.web.integration.resolver;

import io.riggo.data.domain.LoadSubStatus;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class SalesforceRevenovaLoadStatusResolver implements LoadStatusResolver{


    public final static String SALES_STATUS = "sales_status";
    public final static String LOAD_STATUS = "load_status";
    public final static String FIRST_STOP_STATUS = "first_stop_status";
    public final static String LAST_STOP_STATUS = "last_stop_status";
    public final static String HAS_DOCUMENTS = "has_documents";


    public LoadSubStatus resolveLoadStatus(Map<String, Object> parameters){
        String salesStatus = (String) parameters.get(SALES_STATUS);
        String loadStatus = (String) parameters.get(LOAD_STATUS);
        String firstStopStatus = (String) parameters.get(FIRST_STOP_STATUS);
        String lastStopStatus = (String) parameters.get(LAST_STOP_STATUS);
        Boolean hasDocuments = (Boolean) parameters.get(HAS_DOCUMENTS);

        if(StringUtils.equals(StringUtils.trim(salesStatus), "Pending")){
            return LoadSubStatus.QUOTED;
        } else if (StringUtils.equals(StringUtils.trim(salesStatus), "Won")){

            if( StringUtils.equals(StringUtils.trim(loadStatus), "Unassigned") ||
                StringUtils.equals(StringUtils.trim(loadStatus), "Assigned")){
                return LoadSubStatus.BOOKED;
            }
            if( StringUtils.equals(StringUtils.trim(loadStatus), "Dispatched") )
            {
                return LoadSubStatus.DISPATCHED;
            }
            if( StringUtils.equals(StringUtils.trim(loadStatus), "In Transit") &&
                    StringUtils.equals(StringUtils.trim(firstStopStatus), "Arrived"))
            {
                return LoadSubStatus.AT_PICKUP;
            }
            if( StringUtils.equals(StringUtils.trim(loadStatus), "In Transit") &&
                    StringUtils.equals(StringUtils.trim(lastStopStatus), "Arrived"))
            {
                return LoadSubStatus.AT_DELIVERY;
            }
            if( StringUtils.equals(StringUtils.trim(loadStatus), "In Transit") &&
                    StringUtils.equals(StringUtils.trim(firstStopStatus), "Departed"))
            {
                return LoadSubStatus.IN_TRANSIT;
            }
            if( StringUtils.equals(StringUtils.trim(loadStatus), "Delivered") &&
                    (hasDocuments == null || BooleanUtils.isFalse(hasDocuments)))
            {
                return LoadSubStatus.PENDING_DOCUMENTS;
            }
            if( StringUtils.equals(StringUtils.trim(loadStatus), "Delivered") &&
                    BooleanUtils.isTrue(hasDocuments)
            )
            {
                return LoadSubStatus.DOCUMENTS_RECEIVED;
            }
            return LoadSubStatus.QUOTED;
        }
        return LoadSubStatus.NO_STATUS;
    }
}
