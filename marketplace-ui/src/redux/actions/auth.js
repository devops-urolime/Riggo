export const LOGIN_SUCCESS = "LOGIN_SUCCESS";
export const LOGIN_FAIL = "LOGIN_FAIL";
export const LOGIN_REQUEST = "LOGIN_REQUEST";
export const LOGOUT_REQUEST = "LOGOUT_REQUEST";

export function login(message){
  return {
    type: LOGIN_REQUEST,
    message
  };
}

export function loginFail(err){
  return {
    type: LOGIN_FAIL,
    err
  };
}

export function loginSuccess(token, expiresIn){
  return {
    type: LOGIN_SUCCESS,
    token,
    expiresIn
  };
}

export function logOut(){
  return {
    type: LOGOUT_REQUEST
  };
}
