export const BASE_END_POINT='https://o0de6p7v3h.execute-api.us-west-2.amazonaws.com/qa';
export const LOAD_END_POINT='/load';
export const AUTH_CONFIG = {
  domain: 'riggo-qa.auth0.com',
  clientId: '7nAY4GVJGBXQQh0uu3Tf9a1YSPu5Twuv',
  audience:'load-resource-api',
  // Comment or uncomment for local testing when get the JWT.
  callbackUrl: 'http://localhost:3000/callback',
  // Comment or uncomment to set the correct cloudfront when deploy.
  // callbackUrl: 'http://dt2f4aj1no65b.cloudfront.net/callback',
  responseType: 'token id_token',
  scope: 'openid profile read:load'
};

export const APP_PATH_ROOT = "/";
export const APP_PATH_HOME = "/home";
export const APP_PATH_LOAD = "/load";
export const APP_PATH_LOAD_ID = APP_PATH_LOAD + "/:id*";
export const APP_PATH_AUTH0_CALLBACK = "/callback";
