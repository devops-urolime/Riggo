export const APP_API_CALL_FAIL = "APP_API_CALL_FAIL";
export const GET_LOAD = "GET_LOAD";
export const GET_LOAD_SUCCESS = "GET_LOAD_SUCCESS";
export const GET_LOAD_FAIL = "GET_LOAD_FAIL";
export const GET_LOAD_PIPE_LINE_SUMMARY = "GET_LOAD_PIPE_LINE_SUMMARY";
export const GET_LOAD_PIPE_LINE_SUMMARY_SUCCESS = "GET_LOAD_PIPE_LINE_SUMMARY_SUCCESS";
export const GET_LOAD_PIPE_LINE_SUMMARY_FAIL = "GET_LOAD_PIPE_LINE_SUMMARY_FAIL";
export const GET_LOAD_STOP_SUMMARY = "GET_LOAD_STOP_SUMMARY";
export const GET_LOAD_STOP_SUMMARY_SUCCESS = "GET_LOAD_STOP_SUMMARY_SUCCESS";
export const GET_LOAD_STOP_SUMMARY_FAIL = "GET_LOAD_STOP_SUMMARY_FAIL";
export const GET_LOAD_SHIPMENT_SUMMARY = "GET_LOAD_SHIPMENT_SUMMARY";
export const GET_LOAD_SHIPMENT_SUMMARY_SUCCESS = "GET_LOAD_SHIPMENT_SUMMARY_SUCCESS";
export const GET_LOAD_SHIPMENT_SUMMARY_FAIL = "GET_LOAD_SHIPMENT_SUMMARY_FAIL";

export function findLoadById(id) {
  return {
    type: GET_LOAD,
    id
  };
}

export function loadPipeLineSummary() {
  return {
    type: GET_LOAD_PIPE_LINE_SUMMARY
  };
}

export function loadStopSummary() {
  return {
    type: GET_LOAD_STOP_SUMMARY
  };
}

export function loadShipmentSummary(offset, units, fiscalMonth, fiscalYear, week) {
  return {
    type: GET_LOAD_SHIPMENT_SUMMARY,
    offset,
    units,
    fiscalMonth,
    fiscalYear,
    week
  };
}
