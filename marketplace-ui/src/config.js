export const BASE_END_POINT= process.env.REACT_APP_BASE_END_POINT;
export const EMPTY_JWT_ERROR_MESSAGE='There is no JWT access token to make calls to the APIs';
export const STATUS_401_ERROR_MESSAGE='Calls to the API return 401 Unauthorized';
export const STATUS_400_ERROR_MESSAGE='Calls to the API return 400 Bad Request';
export const STATUS_403_ERROR_MESSAGE='Calls to the API return 403 Forbidden';
export const STATUS_500_ERROR_MESSAGE='Calls to the API return 500 Internal Server Error';
export const STATUS_504_ERROR_MESSAGE='Calls to the API return 504 Gateway Timeout';
export const LOAD_END_POINT='/load';
export const MENU_END_POINT='/menus';
export const LOAD_PIPELINE_SUMMARY_END_POINT= LOAD_END_POINT + '/pipeline/summary';
export const LOAD_STOP_SUMMARY_END_POINT= LOAD_END_POINT + '/stop/summary';
export const LOAD_SHIPMENT_SUMMARY_END_POINT= LOAD_END_POINT + '/shipments/summary';
export const LOAD_READ_SCOPE = "read:load";
export const LOAD_WRITE_SCOPE = "write:load";
export const LOAD_READ_PIPE_LINE_SCOPE = "read:loadPipeline";
export const LOAD_WRITE_INVOICE_SCOPE = "write:loadInvoice";
export const LOAD_READ_MENU_SCOPE = "read:menu";
export const PROFILE_SCOPE = "profile";
export const OPENID_SCOPE = "openid";
export const OFFLINE_ACCESS_SCOPE = "offline_access";
export const AUTH_CONFIG_REALM = "Username-Password-Authentication";
export const AUTH_CONFIG = {
  domain: process.env.REACT_APP_DOMAIN_AUTH_CONFIG,
  clientId: process.env.REACT_APP_CLIENT_ID_AUTH_CONFIG,
  audience: process.env.REACT_APP_AUDIENCE_ID_AUTH_CONFIG,
  callbackUrl: process.env.REACT_APP_CALL_BACK_URL_AUTH_CONFIG,
  responseType: 'token id_token',
  scope: `${OPENID_SCOPE} ${PROFILE_SCOPE} ${LOAD_READ_SCOPE} ${LOAD_READ_PIPE_LINE_SCOPE} ${LOAD_WRITE_SCOPE} ${LOAD_WRITE_INVOICE_SCOPE} ${LOAD_READ_MENU_SCOPE} ${OFFLINE_ACCESS_SCOPE}`
};

export const APP_PATH_ROOT = "/";
export const APP_PATH_LOAD = "/load";
export const APP_PATH_LOGIN = "/login";
export const APP_PATH_FORGOT_PASSWORD = "/forgot-password";
export const APP_PATH_DASHBOARD = "/dashboard";
export const APP_PATH_SHIPMENTS = "/shipments";
export const APP_PATH_REPORTS = "/reports";
export const APP_PATH_BILLING = "/billing";
export const APP_PATH_LOAD_ID = APP_PATH_LOAD + "/:id*";
export const APP_PATH_AUTH0_CALLBACK = "/callback";
export const MOCK_ALL_DATA= (process.env.REACT_APP_MOCK_ALL_DATA === 'true');
export const URL_CODE_APP = {
  dashboard: APP_PATH_DASHBOARD,
  shipments: APP_PATH_SHIPMENTS,
  reports: APP_PATH_REPORTS,
  billing: APP_PATH_BILLING,
};

export const MENU_TYPE_POSITION_LEFT = "1";
