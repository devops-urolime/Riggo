export const BASE_END_POINT='https://o0de6p7v3h.execute-api.us-west-2.amazonaws.com/qa';
export const EMPTY_JWT_ERROR_MESSAGE='There is no JWT access token to make calls to the APIs';
export const LOAD_END_POINT='/load';
export const MENU_END_POINT='/menus';
export const LOAD_PIPELINE_SUMMARY_END_POINT= LOAD_END_POINT + '/pipeline/summary';
export const LOAD_STOP_SUMMARY_END_POINT= LOAD_END_POINT + '/stop/summary';
export const LOAD_READ_SCOPE = "read:load";
export const LOAD_READ_PIPE_LINE_SCOPE = "read:loadPipeline";
export const PROFILE_SCOPE = "profile";
export const OPENID_SCOPE = "openid";
export const AUTH_CONFIG_REALM = "Username-Password-Authentication";
export const AUTH_CONFIG = {
  domain: process.env.REACT_APP_DOMAIN_AUTH_CONFIG,
  clientId: process.env.REACT_APP_CLIENT_ID_AUTH_CONFIG,
  audience: process.env.REACT_APP_AUDIENCE_ID_AUTH_CONFIG,
  callbackUrl: process.env.REACT_APP_CALL_BACK_URL_AUTH_CONFIG,
  responseType: 'token id_token',
  scope: `${OPENID_SCOPE} ${PROFILE_SCOPE} ${LOAD_READ_SCOPE} ${LOAD_READ_PIPE_LINE_SCOPE}`
};

export const APP_PATH_ROOT = "/";
export const APP_PATH_HOME = "/home";
export const APP_PATH_LOAD = "/load";
export const APP_PATH_LOGIN = "/login";
export const APP_PATH_LOAD_ID = APP_PATH_LOAD + "/:id*";
export const APP_PATH_AUTH0_CALLBACK = "/callback";
export const MOCK_ALL_DATA= (process.env.REACT_APP_MOCK_ALL_DATA === 'true');
