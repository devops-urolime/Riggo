import { GET_MENU_SUCCESS } from '../actions/menu';
import { createSelector } from 'reselect';

const initState = {
  current: null,
  list:[]
};

export default function(state = initState, action) {
  switch (action.type) {
    case GET_MENU_SUCCESS:
      return {
          ...state,
          list: action.menu
      };
    default:
      return state;
  }
}

const currentMenuSelector = state => state.menu.current;
const listMenuSelector = state => state.menu.list;

export const getCurrentMenu = createSelector(
  currentMenuSelector,
  menuItem => menuItem
);

export const getMenu = createSelector(
  listMenuSelector,
  list => list
);
