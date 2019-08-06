import {
  BASE_END_POINT,
  EMPTY_JWT_ERROR_MESSAGE,
  LOAD_END_POINT,
  LOAD_PIPELINE_SUMMARY_END_POINT,
  LOAD_STOP_SUMMARY_END_POINT,
  MENU_END_POINT,
  MOCK_ALL_DATA,
  STATUS_400_ERROR_MESSAGE,
  STATUS_401_ERROR_MESSAGE,
  STATUS_500_ERROR_MESSAGE
} from './config';

const METHOD_GET = 'get';

const validateEmptyJWT = (jwt) => {
  if (!jwt) { throw new Error(EMPTY_JWT_ERROR_MESSAGE); }
};

const menuMockData = {
    "status": 200,
    "message": "success",
    "data": [
        {
          "id": 1,
          "siteId": 100,
          "name": "Dashboard",
          "type": 1,
          "parentMenuId": 0,
          "urlCode":"dashboard",
          "rank": 1,
          "icon": "dashboard"
        },
        {
          "id": 2,
          "siteId": 100,
          "name": "Shipments",
          "type": 1,
          "parentMenuId": 0,
          "urlCode":"shipments",
          "rank": 2,
          "icon": "shipments"
        },
        {
          "id": 3,
          "siteId": 100,
          "name": "Reports",
          "type": 1,
          "parentMenuId": 0,
          "urlCode":"reports",
          "rank": 3,
          "icon": "reports"
        },
        {
          "id": 4,
          "siteId": 100,
          "name": "Billing",
          "type": 1,
          "parentMenuId": 0,
          "urlCode":"billing",
          "rank": 4,
          "icon": "reports"
        }
    ]
};

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

const loadByIdMock =  {
   "status":200,
   "message":"success",
   "data":{}
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
  validateEmptyJWT(JWT);
  return buildRequestMetaData(method, JWT);
};

const handleStatus = (response) => {
    const apiMessage = (response) => {
      return " -- API message : " + response.message;
    };
    if(response && response.status === 401){
     throw new Error(STATUS_401_ERROR_MESSAGE + apiMessage(response));
    }
    if(response && response.status === 400){
     throw new Error(STATUS_400_ERROR_MESSAGE + apiMessage(response));
    }
    if(response && response.status === 500){
     throw new Error(STATUS_500_ERROR_MESSAGE + apiMessage(response));
    }
};

const consumeApi = async (endPoint, method, JWT, mockData, mockOverride) =>{
  let response = null;
  let json = null;
  if ( MOCK_ALL_DATA || mockOverride){
    console.log(`MOCK_ALL_DATA active for endpoint: ${endPoint}`);
    json = mockData;
  } else {
    response = await fetch(endPoint, buildRequestData(method, JWT));
    json = await response.json();
    handleStatus(response);
  }
  if(json && json.data){
    response = json.data;
  }
  return response;
};

export const findLoadByIdApi = async (idLoad, JWT) => {
    const END_POINT = BASE_END_POINT + LOAD_END_POINT + '/' + idLoad;
    return consumeApi(END_POINT, METHOD_GET, JWT, loadByIdMock);
};

export const loadPipeLineSummaryApi = async (JWT) => {
    const END_POINT = BASE_END_POINT + LOAD_PIPELINE_SUMMARY_END_POINT ;
    return consumeApi(END_POINT, METHOD_GET, JWT, summaryMock);
};

export const getMenuApi = async (JWT, menuTypePosition) => {
    const MENU_TYPE_POSITION = menuTypePosition || "";
    const END_POINT = BASE_END_POINT + MENU_END_POINT + "?type=" + MENU_TYPE_POSITION ;
    return consumeApi(END_POINT, METHOD_GET, JWT, menuMockData);
};

export const loadStopsSummaryApi = async (JWT) => {
    const END_POINT = BASE_END_POINT + LOAD_STOP_SUMMARY_END_POINT ;
    return consumeApi(END_POINT, METHOD_GET, JWT, summaryStopMock);
};
