import { LOGIN_SUCCESS, LOGIN_FAIL, LOGIN_REQUEST, LOGOUT_REQUEST } from "../actions/auth";

const initState = {
  message:"",
  token: null,
  isLogin:false,
  user: null,
  err: null,
};

export default function(state = initState, action) {
  switch (action.type) {
    case LOGIN_SUCCESS:
      return {
          ...state,
          token: action.token,
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
        isLogin: false
      };
    default:
      return state;
  }
}
