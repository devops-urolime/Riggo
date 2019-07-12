import {
  BASE_END_POINT,
  EMPTY_JWT_ERROR_MESSAGE,
  LOAD_END_POINT,
  LOAD_PIPELINE_SUMMARY_END_POINT
} from './config';

const validateEmptyJWT = (jwt) => {
  if (!jwt) { throw new Error(EMPTY_JWT_ERROR_MESSAGE); }
};

export const findLoadByIdApi = async (idLoad) => {
    const END_POINT = BASE_END_POINT + LOAD_END_POINT + '/' + idLoad;
    const ACCESS_TOKEN_JWT = localStorage.getItem('accessToken');
    validateEmptyJWT(ACCESS_TOKEN_JWT);
    const requestData = {
        method: 'get',
        headers: new Headers({
            'Authorization': 'Bearer ' + ACCESS_TOKEN_JWT,
            'Accept': 'application/json'
        })
    };
    const response = await fetch(
        END_POINT,
        requestData
    );
    const json = await response.json();
    return (json) ? json : {};
};

export const loadPipeLineSummaryApi = async () => {
    const END_POINT = BASE_END_POINT + LOAD_PIPELINE_SUMMARY_END_POINT ;
    const ACCESS_TOKEN_JWT = localStorage.getItem('accessToken');
    validateEmptyJWT(ACCESS_TOKEN_JWT);
    const requestData = {
        method: 'get',
        headers: new Headers({
            'Authorization': 'Bearer ' + ACCESS_TOKEN_JWT,
            'Accept': 'application/json'
        })
    };
    const response = await fetch(
        END_POINT,
        requestData
    );
    const json = await response.json();
    let responseData = [];
    if(json && json.data){
      json.data.forEach((item) => {
        const infoItem = Object.entries(item)
        const summary = {
            label: infoItem[0][0],
            number: infoItem[0][1]
        };
        responseData.push(summary);
      });
    }
    return responseData;
};
