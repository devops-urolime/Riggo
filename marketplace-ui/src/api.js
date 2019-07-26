import {
  BASE_END_POINT,
  EMPTY_JWT_ERROR_MESSAGE,
  LOAD_END_POINT,
  LOAD_PIPELINE_SUMMARY_END_POINT, MENU_END_POINT, MOCK_ALL_DATA
} from './config';

const METHOD_GET = 'get';
const validateEmptyJWT = (jwt) => {
  if (!jwt) { throw new Error(EMPTY_JWT_ERROR_MESSAGE); }
};

const mockDataPipeLineSummary = [
  {label: "pending", number :58},
  {label: "in transit", number :30},
  {label: "Delivered", number :28}
];

const buildRequestMetaData = (method, ACCESS_TOKEN_JWT) =>{
   return {
     method: method,
     headers: new Headers({
         'Authorization': 'Bearer ' + ACCESS_TOKEN_JWT,
         'Accept': 'application/json'
     })
   };
};

const buildRequestData = (method) => {
  const ACCESS_TOKEN_JWT = localStorage.getItem('accessToken');
  validateEmptyJWT(ACCESS_TOKEN_JWT);
  return buildRequestMetaData(method, ACCESS_TOKEN_JWT);
};

export const findLoadByIdApi = async (idLoad) => {
    const END_POINT = BASE_END_POINT + LOAD_END_POINT + '/' + idLoad;
    const response = await fetch(END_POINT, buildRequestData(METHOD_GET));
    const json = await response.json();
    return (json && json.load) ? json.load : {};
};

export const loadPipeLineSummaryApi = async () => {
    const END_POINT = BASE_END_POINT + LOAD_PIPELINE_SUMMARY_END_POINT ;
    const response = await fetch(END_POINT, buildRequestData(METHOD_GET));
    const json = await response.json();
    let responseData = [];
    if(json && json.data){
      json.data.forEach((item) => {
        const infoItem = Object.entries(item);
        const summary = {
            label: infoItem[0][0],
            number: infoItem[0][1]
        };
        responseData.push(summary);
      });
    }
    if (MOCK_ALL_DATA) console.log(`MOCK_ALL_DATA active for endpoint: ${END_POINT}`);
    return (MOCK_ALL_DATA) ? mockDataPipeLineSummary : responseData;
};

export const getMenuApi = async () => {
    const END_POINT = BASE_END_POINT + MENU_END_POINT ;
    const response = await fetch(END_POINT, buildRequestData(METHOD_GET));
    const json = await response.json();
    let responseData = [];
    if(json && json.data){
      responseData = json.data;
    }
    return responseData;
};
