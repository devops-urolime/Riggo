import { LOGIN_SUCCESS, LOGIN_FAIL, LOGIN_REQUEST, LOGOUT_REQUEST } from "../actions/auth";
import { createSelector } from 'reselect';

const initState = {
  message:"",
  token: null,
  expiresIn: 0,
  isLogin:false,
  user: null,
  err: null,
};

export default function(state = initState, action) {
  switch (action.type) {
    case LOGIN_SUCCESS:
      return {
          ...state,
          token: (action.token.token) ? action.token.token : action.token,
          expiresIn: action.expiresIn,
          isLogin: true
      };
    case LOGIN_REQUEST:
      return {
          ...state,
          message: action.message
      };
    case LOGIN_FAIL:
      return {
          ...state,
          err: action.err
      };
    case LOGOUT_REQUEST:
      return {
        ...state,
        isLogin: false,
        token: null,
        expiresAt: 0
      };
    default:
      return state;
  }
}

const currentTokenSelector = state => state.auth.token;
const isLoginSelector = state => state.auth.isLogin;

export const getToken = createSelector(
  currentTokenSelector,
  defaultMenu => defaultMenu
);

export const isLogin = createSelector(
  isLoginSelector,
  isLogin => isLogin
);
