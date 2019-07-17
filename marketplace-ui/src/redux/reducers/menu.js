import { GET_MENU_SUCCESS, SET_DEFAULT_MENU } from '../actions/menu';
import { createSelector } from 'reselect';

const initState = {
  current: null,
  list:[],
  defaultMenu: null,
};

export default function(state = initState, action) {
  switch (action.type) {
    case GET_MENU_SUCCESS:
      return {
          ...state,
          list: action.menu
      };
    case SET_DEFAULT_MENU:
      return {
        ...state,
        defaultMenu: action.menuItem
      };
    default:
      return state;
  }
}

const currentMenuSelector = state => state.menu.current;
const listMenuSelector = state => state.menu.list;
const defaultMenuSelector = state => state.menu.defaultMenu;

export const getDefaultMenu = createSelector(
  defaultMenuSelector,
  defaultMenu => defaultMenu
);

export const getCurrentMenu = createSelector(
  currentMenuSelector,
  menuItem => menuItem
);

export const getMenu = createSelector(
  listMenuSelector,
  list => list
);
