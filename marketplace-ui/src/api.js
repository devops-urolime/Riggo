import {
  BASE_END_POINT,
  EMPTY_JWT_ERROR_MESSAGE,
  LOAD_END_POINT,
  LOAD_PIPELINE_SUMMARY_END_POINT, LOAD_STOP_SUMMARY_END_POINT, MENU_END_POINT, MOCK_ALL_DATA
} from './config';

import { JWTLocalStorage } from './lib/auth';

const METHOD_GET = 'get';

const validateEmptyJWT = (jwt) => {
  if (!jwt) { throw new Error(EMPTY_JWT_ERROR_MESSAGE); }
};

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

const summaryMock = {
   "status":200,
   "message":"success",
   "data":[
      {
         "id":1,
         "name":"Pending",
         "count":26,
         "subStatuses":[
            {
               "id":1,
               "name":"Quoted",
               "count":30
            },
            {
               "id":2,
               "name":"Booked",
               "count":20
            }
         ]
      },
      {
         "id":2,
         "name":"In Transit",
         "count":4,
         "subStatuses":[
            {
               "id":3,
               "name":"Dispatched",
               "count":30
            },
            {
               "id":4,
               "name":"@Pickup",
               "count":40
            },
            {
               "id":5,
               "name":"In transit",
               "count":50
            },
            {
               "id":6,
               "name":"@Delivery",
               "count":60
            }
         ]
      },
      {
         "id":3,
         "name":"Delivered",
         "count":0,
         "subStatuses":[
            {
               "id":7,
               "name":"Pending Docs",
               "count":70
            },
            {
               "id":8,
               "name":"Docs Received",
               "count":80
            },
           {
              "id":8,
              "name":"Invoiced",
              "count":30
           }
         ]
      }
   ]
};

const summaryStopMock = {
   "status":200,
   "message":"success",
   "data":[
      {
         "id":1,
         "name":"Pickup",
         "data":[
            {
               "id":1,
               "name":"Early",
               "count":12
            },
            {
               "id":2,
               "name":"On Time",
               "count":26
            },
            {
               "id":3,
               "name":"Delayed",
               "count":50
            }
         ]
      },
      {
         "id":2,
         "name":"Delivery",
         "data":[
            {
               "id":1,
               "name":"Early",
               "count":12
            },
            {
               "id":2,
               "name":"On Time",
               "count":26
            },
            {
               "id":3,
               "name":"Delayed",
               "count":50
            }
         ]
      }
   ]
};

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
  const ACCESS_TOKEN_JWT = JWT || localStorage.getItem(JWTLocalStorage);
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
    let responseData = null;
    let json = null;
    if (MOCK_ALL_DATA){
      console.log(`MOCK_ALL_DATA active for endpoint: ${END_POINT}`);
      json = summaryMock;
    } else {
      const response = await fetch(END_POINT, buildRequestData(METHOD_GET, JWT));
      json = await response.json();
    }
    if(json && json.data){
      responseData = json.data;
    }
    return responseData;
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

export const loadStopsSummaryApi = async (JWT) => {
    const END_POINT = BASE_END_POINT + LOAD_STOP_SUMMARY_END_POINT ;
    let responseData = null;
    let json = null;
    if (MOCK_ALL_DATA){
      console.log(`MOCK_ALL_DATA active for endpoint: ${END_POINT}`);
      json = summaryStopMock;
    } else {
      const response = await fetch(END_POINT, buildRequestData(METHOD_GET, JWT));
      json = await response.json();
    }
    if(json && json.data){
      responseData = json.data;
    }
    return responseData;
};
