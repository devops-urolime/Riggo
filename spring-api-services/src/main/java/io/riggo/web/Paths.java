package io.riggo.web;

public class Paths {
    public static final String API = "/api/";
    public static final String VERSION = "v1";
    public static final String API_VERSION = API + VERSION;

    public static final String MENUS = "/menus";

    public static final String LOAD = "/load";
    public static final String LOADID_PARAM = "/{loadId}";

    public static final String PIPELINE_SUMMARY = "/pipeline/summary";
    public static final String SHIPMENTS_SUMMARY = "/shipments/summary";
    public static final String STOP_SUMMARY = "/stop/summary";

    public static final String QUOTE = "/quote";
    public static final String STOP = "/stop";
    public static final String LINE_ITEM = "/line-item";

    public static final String LOAD_PIPELINE_SUMMARY = LOAD + PIPELINE_SUMMARY;
    public static final String LOAD_SHIPMENT_SUMMARY = LOAD + SHIPMENTS_SUMMARY;
    public static final String LOAD_STOP_SUMMARY = LOAD + STOP_SUMMARY;
    public static final String LOAD_LOADID_PARAM = LOAD + LOADID_PARAM;

    public static final String LOAD_QUOTE = LOAD + QUOTE;
    public static final String LOAD_LOADID_PARAM_QUOTE = LOAD + LOADID_PARAM + QUOTE;

    public static final String LOAD_STOP = LOAD + STOP;
    public static final String LOAD_LOADID_PARAM_STOP = LOAD + LOADID_PARAM + STOP;

    public static final String LOAD_LINE_ITEM = LOAD + LINE_ITEM;
    public static final String LOAD_LOADID_PARAM_LINE_ITEM = LOAD + LOADID_PARAM + LINE_ITEM;
}
