package io.riggo.web;

public class Paths {
    public static final String API = "/api/";
    public static final String VERSION = "v1";
    public static final String API_VERSION = API + VERSION;

    public static final String LOAD = "/load";
    public static final String LOAD_PIPELINE_SUMMARY = "/load/pipeline/summary";
    public static final String LOAD_SHIPMENT_SUMMARY = "/load/shipments/summary";
    public static final String LOAD_STOP_SUMMARY = "/load/stop/summary";
    public static final String MENUS = "/menus";

    public static final String LOADID_PARAM = "/{loadid}";
    public static final String INVOICE = "/invoice";



    public static final String API_VERSION_LOAD = API + VERSION + LOAD;
    public static final String API_VERSION_LOAD_PIPELINE_SUMMARY = API + VERSION + LOAD_PIPELINE_SUMMARY;
    public static final String API_VERSION_LOAD_SHIPMENT_SUMMARY = API + VERSION + LOAD_SHIPMENT_SUMMARY;
    public static final String API_VERSION_MENUS = API + VERSION + MENUS;

    public static final String API_VERSION_LOAD_INVOICE = API + VERSION + LOAD + INVOICE;
    public static final String API_VERSION_LOAD_LOADID_PARAM_INVOICE = API + VERSION + LOAD + LOADID_PARAM + INVOICE;
}
