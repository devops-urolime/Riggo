import { GET_LOAD_SUCCESS } from '../actions/load';
import { createSelector } from 'reselect';

const initState = {
  current: null,
  list:[]
};

export default function(state = initState, action) {
  switch (action.type) {
    case GET_LOAD_SUCCESS:
      return {
          ...state,
          current: action.load
      };
    default:
      return state;
  }
}

const currentLoadSelector = state => state.load.current;

export const getCurrentLoad = createSelector(
  currentLoadSelector,
  current => current
);
