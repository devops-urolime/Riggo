import React from 'react';
import PropTypes from 'prop-types';
import { Redirect } from 'react-router-dom';
import queryString from 'querystring';

const Callback = ({auth, location:{hash}, loginSuccess, loginFail}) => {
  const parsedHash = queryString.parse(hash.substring(1,hash.length), { ignoreQueryPrefix: true });
  const hasToken = parsedHash.access_token;
  if(hasToken){
    loginSuccess({token: hasToken});
  } else {
    loginFail({message:"No access token in callback"});
  }
  return hasToken ? (
    <Redirect to="/load/1"/>
  ) : (
    <div>
      <p>Loading...</p>
    </div>
  );
};

Callback.propTypes = {
  auth: PropTypes.object,
  location: PropTypes.object,
  loginSuccess: PropTypes.func,
  loginFail: PropTypes.func,
};

export default Callback;
