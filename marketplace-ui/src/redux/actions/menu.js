export const GET_MENU = "GET_MENU";
export const GET_MENU_SUCCESS = "GET_MENU_SUCCESS";
export const GET_MENU_FAIL = "GET_MENU_FAIL";
export const SET_DEFAULT_MENU = "SET_DEFAULT_MENU";
export const SET_CURRENT_MENU = "SET_CURRENT_MENU";
export const SET_OPEN_MENU = "SET_OPEN_MENU";

export function loadMenu(menuTypePosition) {
  return {
    type: GET_MENU,
    menuTypePosition
  };
}

export function setCurrentMenu(menu) {
  return {
    type: SET_CURRENT_MENU,
    menu
  };
}

export function setOpenMenu(openMenu) {
  return {
    type: SET_OPEN_MENU,
    openMenu
  };
}

