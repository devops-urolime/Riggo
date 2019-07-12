export const BASE_END_POINT='https://o0de6p7v3h.execute-api.us-west-2.amazonaws.com/qa';
export const EMPTY_JWT_ERROR_MESSAGE='There is no JWT access token to make calls to the APIs';
export const LOAD_END_POINT='/load';
export const LOAD_PIPELINE_SUMMARY_END_POINT= LOAD_END_POINT + '/pipeline/summary';
export const LOAD_READ_SCOPE = "read:load";
export const LOAD_READ_PIPE_LINE_SCOPE = "read:loadPipeline";
export const PROFILE_SCOPE = "profile";
export const OPENID_SCOPE = "openid";
export const AUTH_CONFIG = {
  domain: 'riggo-qa.auth0.com',
  clientId: '7nAY4GVJGBXQQh0uu3Tf9a1YSPu5Twuv',
  audience:'load-resource-api',
  // Comment or uncomment for local testing when get the JWT.
  callbackUrl: 'http://localhost:3000/callback',
  // Comment or uncomment to set the correct cloudfront when deploy.
  // callbackUrl: 'http://dt2f4aj1no65b.cloudfront.net/callback',
  responseType: 'token id_token',
  scope: `${OPENID_SCOPE} ${PROFILE_SCOPE} ${LOAD_READ_SCOPE} ${LOAD_READ_PIPE_LINE_SCOPE}`
};

export const APP_PATH_ROOT = "/";
export const APP_PATH_HOME = "/home";
export const APP_PATH_LOAD = "/load";
export const APP_PATH_LOAD_ID = APP_PATH_LOAD + "/:id*";
export const APP_PATH_AUTH0_CALLBACK = "/callback";
