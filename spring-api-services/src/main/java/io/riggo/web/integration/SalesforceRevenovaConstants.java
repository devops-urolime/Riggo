package io.riggo.web.integration;

public interface SalesforceRevenovaConstants {

    String[] POST_PUT_LOAD_IGNORE_PROPERTIES = new String[]{"id", "extSysTenantId", "locationBasedSvcsReq", "createdAt", "updatedAt", "deleted", "referenceNumber", "billOfLadingNo", "carrierQuoteTotal", "carrierInvoiceTotal", "customerQuoteTotal", "customerQuoteTotal", "customerTransportTotal"};
    String[] PUT_LOAD_LINE_ITEM_IGNORE_PROPERTIES = new String[]{"extSysTenantId", "description", "hazMat", "deliveryStopExtSysId", "pickupStopExtSysId", "loadExtSysId", "weight", "name"};
    String[] POST_LOAD_LOAD_STOP_IGNORE_PROPERTIES = new String[]{"extSysId", "extSysTenantId", "name", "type", "stopNumber", "shippingReceivingHours", "expectedDateTime", "appointmentRequired", "appointmentTime", "stopStatus", "carrierStatus"};
    String[] PUT_LOAD_STOP_IGNORE_PROPERTIES = new String[]{"extSysId", "extSysTenantId", "name", "type", "stopNumber", "shippingReceivingHours", "expectedDateTime", "appointmentRequired", "appointmentTime", "arrivalDate", "departureDateTime", "stopStatus", "carrierStatus"};
    String[] POST_PUT_SHIPPER_IGNORE_PROPERTIES = new String[]{"extSysTenantId", "name"};
    String[] POST_PUT_LOAD_STOP_LOCATION_IGNORE_PROPERTIES = new String[]{"extSysTenantId", "name"};
    String[] POST_PUT_LOAD_STOP_LOCATION_ADDRESS_IGNORE_PROPERTIES = new String[]{"extSysTenantId", "name"};
    String[] POST_PUT_QUOTE_IGNORE_PROPERTIES = new String[]{"extSysId", "loadId", "quoteDate", "status", "comments", "netFreightCharges", "fuelSurcharge"};
}