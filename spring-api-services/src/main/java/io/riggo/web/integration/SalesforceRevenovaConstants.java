package io.riggo.web.integration;

public interface SalesforceRevenovaConstants {

    String[] POST_PUT_LOAD_IGNORE_PROPERTIES = new String[]{"id", "extSysTenantId", "locationBasedSvcsReq", "createdAt", "updatedAt", "deleted", "referenceNumber", "billOfLadingNo", "carrierQuoteTotal", "carrierInvoiceTotal", "customerQuoteTotal", "customerQuoteTotal", "customerTransportTotal"};
    String[] PATCH_LOAD_LOAD_LINE_ITEM_IGNORE_PROPERTIES = new String[]{"id", "extSysTenantId", "pickupStop", "deliveryStop", "rank", "loadId", "deleted"};
    String[] PATCH_LOAD_LOAD_STOP_IGNORE_PROPERTIES = new String[]{"id", "extSysTenantId", "name", "shippingReceivingHours", "location", "stopNumber", "type", "expectedDateTime", "appointmentRequired", "appointmentTime", "stopStatus", "carrierStatus", "arrivalStatus", "departureStatus", "arrivalDate", "departureDateTime"};
    String[] POST_PUT_SHIPPER_IGNORE_PROPERTIES = new String[]{"extSysTenantId", "name"};
}