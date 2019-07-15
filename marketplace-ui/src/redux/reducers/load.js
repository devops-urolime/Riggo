import { GET_LOAD_PIPE_LINE_SUMMARY_SUCCESS, GET_LOAD_SUCCESS } from '../actions/load';
import { createSelector } from 'reselect';

const initState = {
  current: null,
  list:[],
  pipeLineSummary:[]
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
    default:
      return state;
  }
}

const currentLoadSelector = state => state.load.current;
const pipeLineSummarySelector = state => state.load.pipeLineSummary;

export const getCurrentLoad = createSelector(
  currentLoadSelector,
  current => current
);

export const getPipeLineSummary = createSelector(
  pipeLineSummarySelector,
  pipeLineSummary => pipeLineSummary
);
