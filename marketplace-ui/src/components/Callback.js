import React from 'react';
import PropTypes from 'prop-types';
import { Redirect } from 'react-router-dom';
import queryString from 'querystring';
import { APP_PATH_LOGIN, APP_PATH_ROOT } from '../config';
import { IS_LOGGED_IN_LOCAL_STORAGE_TRUE, saveJWTInfoToLocalStorage } from '../lib/auth';

const parseSaveInfoToken = (hash) => {
  const parsedHash = queryString.parse(hash.substring(1,hash.length), { ignoreQueryPrefix: true });
  const baseHash = (parsedHash.access_token) ? parsedHash.access_token.split("&") : [];
  let token = false;
  let expiresIn = 0;
  if(baseHash.length > 0){
    token = baseHash[0];
    const expiresInData = baseHash.filter( param => param.indexOf("expires_in") === 0 ).map(
      (data) => data.split("=")[1]
    );
    expiresIn = (expiresInData.length > 0) ? parseInt(expiresInData[0], 10) : expiresIn;
    saveJWTInfoToLocalStorage(token, expiresIn, IS_LOGGED_IN_LOCAL_STORAGE_TRUE);
  }
  return {token, expiresIn};
};

const Callback = ({ location:{hash}, loginSuccess, loginFail}) => {
  const result = parseSaveInfoToken(hash);
  if(result.token){
    loginSuccess(result.token, result.expiresIn);
  } else {
    loginFail({message:"No access token in callback"});
  }
  return result.token ?
     <Redirect to={APP_PATH_ROOT}/> :
     <Redirect to={APP_PATH_LOGIN}/> ;
};

Callback.propTypes = {
  location: PropTypes.object,
  loginSuccess: PropTypes.func,
  loginFail: PropTypes.func,
};

export default Callback;
