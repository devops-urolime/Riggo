import { WebAuth } from 'auth0-js';
import { AUTH_CONFIG } from '../config';
import { loginSuccess, loginFail, logOut } from '../redux/actions/auth';

export const JWT_LOCAL_STORAGE = 'token';
export const EXPIRES_IN_LOCAL_STORAGE = 'expiresIn';
export const IS_LOGGED_IN_LOCAL_STORAGE = 'isLoggedIn';
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
  localStorage.removeItem(IS_LOGGED_IN_LOCAL_STORAGE);
  localStorage.removeItem(JWT_LOCAL_STORAGE);
  logOut();
  webAuth.logout({
    returnTo: window.location.origin
  });
};

export const isNotExpired = (expiresIn) => {
  let expiresAt = (expiresIn * 1000) + new Date().getTime();
  return new Date().getTime() < expiresAt;
};

export const handleAuthentication = () =>  {
  webAuth.parseHash((err, authResult) => {
    if (authResult && authResult.accessToken && authResult.idToken) {
      const { accessToken, expiresIn} = authResult;
      loginSuccess(accessToken, expiresIn);
      // Set accessToken in localStorage
      localStorage.setItem(JWT_LOCAL_STORAGE, accessToken);
      localStorage.setItem(EXPIRES_IN_LOCAL_STORAGE, expiresIn);
      localStorage.setItem(IS_LOGGED_IN_LOCAL_STORAGE, 'true');
    } else if (err) {
      loginFail({err});
    }
  });
};

