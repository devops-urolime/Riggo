import { WebAuth } from 'auth0-js';
import { AUTH_CONFIG } from '../config';
import { loginSuccess, loginFail, logOut } from '../redux/actions/auth';

export const JWTLocalStorage = 'token';
export const isLoggedInLocalStorage = 'isLoggedIn';
export const webAuth = new WebAuth({
  domain: AUTH_CONFIG.domain,
  clientID: AUTH_CONFIG.clientId,
  redirectUri: AUTH_CONFIG.callbackUrl,
  audience: AUTH_CONFIG.audience,
  responseType: AUTH_CONFIG.responseType,
  scope: AUTH_CONFIG.scope
});

export const logout = () => {
  // Remove isLoggedIn flag from localStorage
  localStorage.removeItem(isLoggedInLocalStorage);
  localStorage.removeItem(JWTLocalStorage);
  logOut();
  webAuth.logout({
    returnTo: window.location.origin
  });
};

export const isAuthenticated = (expiresAt) => {
  // Check whether the current time is past the
  // access token's expiry time
  return new Date().getTime() < expiresAt;
};

export const renewSession = () => {
  webAuth.checkSession({}, (err, authResult) => {
     if (authResult && authResult.accessToken && authResult.idToken) {
       // Set the time that the access token will expire at
       let expiresAt = (authResult.expiresIn * 1000) + new Date().getTime();
       loginSuccess(authResult.accessToken, expiresAt);
     } else if (err) {
       loginFail({err});
     }
  });
};

export const handleAuthentication = () =>  {
  webAuth.parseHash((err, authResult) => {
    if (authResult && authResult.accessToken && authResult.idToken) {
      // Set the time that the access token will expire at
      let expiresAt = (authResult.expiresIn * 1000) + new Date().getTime();
      loginSuccess(authResult.accessToken, expiresAt);
      // Set accessToken in localStorage
      // localStorage.setItem(JWTLocalStorage, authResult.accessToken);
    } else if (err) {
      loginFail({err});
    }
  });
};

