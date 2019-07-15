export const GET_MENU = "GET_MENU";
export const GET_MENU_SUCCESS = "GET_MENU_SUCCESS";
export const GET_MENU_FAIL = "GET_MENU_FAIL";
export const SET_DEFAULT_MENU = "SET_DEFAULT_MENU";

export function loadMenu() {
  return {
    type: GET_MENU
  };
}

