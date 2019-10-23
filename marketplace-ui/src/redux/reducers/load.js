import {
  GET_LOAD_PIPE_LINE_SUMMARY_SUCCESS, GET_LOAD_SHIPMENT_SUMMARY_SUCCESS,
  GET_LOAD_STOP_SUMMARY_SUCCESS,
  GET_LOAD_SUCCESS,
  UPDATE_NAVIGATION_SHIPMENT_SUMMARY
} from '../actions/load';
import { createSelector } from 'reselect';
export const SHIPMENT_RESULT_BY_MONTH = "months";
export const SHIPMENT_RESULT_BY_WEEK = "weeks";
export const SHIPMENT_RESULT_BY_DAY = "days";

const initState = {
  current: null,
  list:[],
  pipeLineSummary:[],
  stopSummary:[],
  shipmentSummary:[],
  viewTypeShipment: SHIPMENT_RESULT_BY_MONTH,
  itemVizBar: {},
  navCursorOffset: 0,
};

export default function(state = initState, action) {
  switch (action.type) {
    case GET_LOAD_SUCCESS:
      return {
          ...state,
          current: action.load
      };
    case GET_LOAD_PIPE_LINE_SUMMARY_SUCCESS:
          return {
              ...state,
            pipeLineSummary: action.pipeLineSummary
          };
    case GET_LOAD_STOP_SUMMARY_SUCCESS:
      return {
          ...state,
        stopSummary: action.stopSummary
      };
    case GET_LOAD_SHIPMENT_SUMMARY_SUCCESS:
      return {
          ...state,
        shipmentSummary: action.shipmentSummary
      };
    case UPDATE_NAVIGATION_SHIPMENT_SUMMARY:
      return {
          ...state,
        viewTypeShipment: action.viewTypeShipment,
        itemVizBar: action.itemVizBar,
        navCursorOffset: action.navCursorOffset
      };
    default:
      return state;
  }
}

const currentLoadSelector = state => state.load.current;
const pipeLineSummarySelector = state => state.load.pipeLineSummary;
const stopSummarySelector = state => state.load.stopSummary;
const shipmentSummarySelector = state => state.load.shipmentSummary;
const viewTypeShipmentSelector = state => state.load.viewTypeShipment;
const itemVizBarSelector = state => state.load.itemVizBar;
const navCursorOffsetSelector = state => state.load.navCursorOffset;

export const getCurrentLoad = createSelector(
  currentLoadSelector,
  current => current
);

export const getPipeLineSummary = createSelector(
  pipeLineSummarySelector,
  pipeLineSummary => pipeLineSummary
);

export const getStopSummary = createSelector(
  stopSummarySelector,
  stopSummary => stopSummary
);

export const getShipmentSummary = createSelector(
  shipmentSummarySelector,
  shipmentSummary => shipmentSummary
);
export const getViewTypeShipment = createSelector(
  viewTypeShipmentSelector,
  viewTypeShipment => viewTypeShipment
);
export const getItemVizBar = createSelector(
  itemVizBarSelector,
  itemVizBar => itemVizBar
);
export const getNavCursorOffset = createSelector(
  navCursorOffsetSelector,
  navCursorOffset => navCursorOffset
);
