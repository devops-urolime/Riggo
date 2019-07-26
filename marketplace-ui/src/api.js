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

const pipeLineSummaryMockData = [
  {label: "pending", number :58},
  {label: "in transit", number :30},
  {label: "Delivered", number :28}
];

const menuMockData = [
		{
			"id":null,
			"siteId":null,
			"name":"Dashboard",
			"type":null,
			"parentMenuId":0,
			"url":"/?url=Dashboard",
			"rank":1
		},
		{
			"id":null,
			"siteId":null,
			"name":"Shipments",
			"type":null,
			"parentMenuId":0,
			"url":"/?url=Shipments",
			"rank":2
		},
		{
			"id":null,
			"siteId":null,
			"name":"Reports",
			"type":null,
			"parentMenuId":0,
			"url":"/?url=Reports",
			"rank":3
		},
		{
			"id":null,
			"siteId":null,
			"name":"Billing",
			"type":null,
			"parentMenuId":0,
			"url":"/?url=Billing",
			"rank":4
		}
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

const buildRequestData = (method, JWT) => {
  const ACCESS_TOKEN_JWT = JWT || localStorage.getItem('accessToken');
  validateEmptyJWT(ACCESS_TOKEN_JWT);
  return buildRequestMetaData(method, ACCESS_TOKEN_JWT);
};

export const findLoadByIdApi = async (idLoad, JWT) => {
    const END_POINT = BASE_END_POINT + LOAD_END_POINT + '/' + idLoad;
    const response = await fetch(END_POINT, buildRequestData(METHOD_GET, JWT));
    const json = await response.json();
    return (json && json.load) ? json.load : {};
};

export const loadPipeLineSummaryApi = async (JWT) => {
    const END_POINT = BASE_END_POINT + LOAD_PIPELINE_SUMMARY_END_POINT ;
    const response = await fetch(END_POINT, buildRequestData(METHOD_GET, JWT));
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
    return (MOCK_ALL_DATA) ? pipeLineSummaryMockData : responseData;
};

export const getMenuApi = async (JWT) => {
    const END_POINT = BASE_END_POINT + MENU_END_POINT ;
    const response = await fetch(END_POINT, buildRequestData(METHOD_GET, JWT));
    const json = await response.json();
    let responseData = [];
    if(json && json.data){
      responseData = json.data;
    }
    if (MOCK_ALL_DATA) console.log(`MOCK_ALL_DATA active for endpoint: ${END_POINT}`);
    return (MOCK_ALL_DATA) ? menuMockData : responseData;
};
