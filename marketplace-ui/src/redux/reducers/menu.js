import {
  GET_MENU_SUCCESS,
  SET_CURRENT_MENU,
  SET_DEFAULT_MENU,
  SET_OPEN_MENU
} from '../actions/menu';
import { createSelector } from 'reselect';

const initState = {
  current: null,
  list:[],
  defaultMenu: null,
  openMenu: false,
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
        defaultMenu: action.menuItem,
        current: action.menuItem
      };
    case SET_CURRENT_MENU:
      return {
        ...state,
        current: action.menu
      };
    case SET_OPEN_MENU:
      return {
        ...state,
        openMenu: action.openMenu
      };
    default:
      return state;
  }
}

const currentMenuSelector = state => state.menu.current;
const listMenuSelector = state => state.menu.list;
const defaultMenuSelector = state => state.menu.defaultMenu;
const openMenuSelector = state => state.menu.openMenu;

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
export const getOpenMenu = createSelector(
  openMenuSelector,
  list => list
);
