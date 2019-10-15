import { WebAuth } from 'auth0-js';
import { AUTH_CONFIG } from '../config';
import { loginSuccess, loginFail, logOut } from '../redux/actions/auth';

export const JWT_LOCAL_STORAGE = 'token';
export const EXPIRES_IN_LOCAL_STORAGE = 'expiresIn';
export const EXPIRATION_DATE_IN_LOCAL_STORAGE = 'expirationDate';
export const IS_LOGGED_IN_LOCAL_STORAGE = 'isLoggedIn';
export const IS_LOGGED_IN_LOCAL_STORAGE_TRUE = 'true';
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
      const expirationDate = Date.now() + Number.parseInt(expiresIn) * 1000;
      saveJWTInfoToLocalStorage(
        accessToken,
        expiresIn,
        IS_LOGGED_IN_LOCAL_STORAGE_TRUE,
        expirationDate
      );
    } else if (err) {
      loginFail({err});
    }
  });
};

export const saveJWTInfoToLocalStorage= (accessToken, expiresIn, isLogin, expirationDate) =>{
  // Set accessToken in localStorage
  localStorage.setItem(JWT_LOCAL_STORAGE, accessToken);
  localStorage.setItem(EXPIRES_IN_LOCAL_STORAGE, expiresIn);
  localStorage.setItem(EXPIRATION_DATE_IN_LOCAL_STORAGE, expirationDate);
  localStorage.setItem(IS_LOGGED_IN_LOCAL_STORAGE, isLogin);
};

export const isExpiredJWT = () =>{
  const expirationDate = new Date(Number.parseInt(localStorage.getItem(EXPIRATION_DATE_IN_LOCAL_STORAGE)));
  return expirationDate < new Date();
};

export const isValidSession = () => {
    let isValidSession = true;
    const token = localStorage.getItem( JWT_LOCAL_STORAGE );
    const expirationDate = localStorage.getItem( EXPIRATION_DATE_IN_LOCAL_STORAGE );
    // if There is no access token present in local storage, meaning that you are not logged in.
    // or There is an expired access token in local storage.
    if (!token || isExpiredJWT()) {
        isValidSession = false;
    } else {
      console.log(`There is an access token in local storage, and it expires on ${new Date(expirationDate*1000)}.`);
    }
    return isValidSession;
};

export const renewToken = () => {
  // Get a new token using silent authentication.
  webAuth.checkSession({
    },  (err, result) =>  {
      if (err) {
          console.log(`Could not get a new token using silent authentication (${err.error}). Redirecting to login page...`);

      } else {
        // Pulled a new token
        const { accessToken, expiresIn} = result;
          saveJWTInfoToLocalStorage(
            accessToken,
            expiresIn,
            IS_LOGGED_IN_LOCAL_STORAGE_TRUE,
            Date.now() + Number.parseInt(expiresIn) * 1000
          );
          loginSuccess(accessToken, expiresIn);
      }
  });
};
