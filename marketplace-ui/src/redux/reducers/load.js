import {
  GET_LOAD_PIPE_LINE_SUMMARY_SUCCESS,
  GET_LOAD_STOP_SUMMARY_SUCCESS,
  GET_LOAD_SUCCESS
} from '../actions/load';
import { createSelector } from 'reselect';

const initState = {
  current: null,
  list:[],
  pipeLineSummary:[],
  stopSummary:[]
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
    default:
      return state;
  }
}

const currentLoadSelector = state => state.load.current;
const pipeLineSummarySelector = state => state.load.pipeLineSummary;
const stopSummarySelector = state => state.load.stopSummary;

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
