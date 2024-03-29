import {
  BASE_END_POINT,
  EMPTY_JWT_ERROR_MESSAGE,
  LOAD_END_POINT,
  LOAD_PIPELINE_SUMMARY_END_POINT, LOAD_SHIPMENT_SUMMARY_END_POINT,
  LOAD_STOP_SUMMARY_END_POINT,
  MENU_END_POINT,
  MOCK_ALL_DATA,
  STATUS_400_ERROR_MESSAGE,
  STATUS_401_ERROR_MESSAGE, STATUS_403_ERROR_MESSAGE,
  STATUS_500_ERROR_MESSAGE, STATUS_504_ERROR_MESSAGE
} from './config';
import { isValidSession, renewToken } from './lib/auth';
import {
  SHIPMENT_RESULT_BY_DAY,
  SHIPMENT_RESULT_BY_MONTH,
  SHIPMENT_RESULT_BY_WEEK
} from './redux/reducers/load';

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
          "icon": "billing"
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

const shipmentsSummaryMonthlyMock = {
   "status":200,
   "message":"success",
   "data":[
      {
         "title":"Monthly",
         "units":"months",
         // TODO: add total properties at this level to show in the shipments visualization section agree this with Backend.
         "totalShipmentsInPeriod": 382,
         "costPerMlInPeriod": 1.89,
         "totalCostInPeriod": 371450,
         "shipmentData":[
          {
            "label":"April 2019",
            "shipments":15,
            "costPerMile":3.7,
            "totalMiles": 25000,
            "fiscalMonth":7,
            "fiscalYear":2019,
            "fiscalWeek":0,
            "offset": 0,
            "totalCost": 3000.837565
          },
          {
           "label":"May 2019",
           "shipments":65,
           "costPerMile":8.57,
           "totalMiles": 2500,
           "fiscalMonth":7,
           "fiscalYear":2019,
           "fiscalWeek":0,
           "offset": 0,
           "totalCost": 3000.837565
          },
          {
             "label":"June 2019",
             "shipments":35,
             "costPerMile":3.20,
             "totalMiles": 25000,
             "fiscalMonth":7,
             "fiscalYear":2019,
             "fiscalWeek":0,
             "offset": 0,
             "totalCost": 3000.837565
          },
          {
             "label":"July 2019",
             "shipments":40,
             "costPerMile":4.10,
             "totalMiles": 2500,
             "fiscalMonth":7,
             "fiscalYear":2019,
             "fiscalWeek":0,
             "offset": 0,
             "totalCost": 3000.837565
          }

         ]
      }
   ]
};

const shipmentsSummaryWeeklyMock = {
   "status":200,
   "message":"success",
   "data":[
      {
         "title":"July 2019",
         "units":"weeks",
         "shipmentData":[
            {
               "label":"Week 1",
               "shipments":30,
               "costPerMile":2.99,
               "fiscalMonth":7,
               "totalMiles": 25,
               "fiscalYear":2019,
               "fiscalWeek":1,
               "offset": 0,
              "totalCost": 3000.837565
            },
            {
               "label":"Week 2",
               "shipments":40,
               "costPerMile":1.99,
               "fiscalMonth":7,
               "totalMiles": 25,
               "fiscalYear":2019,
               "fiscalWeek":2,
               "offset": 0,
               "totalCost": 3000.837565
            },
            {
               "label":"Week 3",
               "shipments":50,
               "costPerMile":3.69,
               "fiscalMonth":7,
               "totalMiles": 25,
               "fiscalYear":2019,
               "fiscalWeek":3,
               "offset": 0,
               "totalCost": 3000.837565
            },
            {
               "label":"Week 4",
               "shipments":20,
               "costPerMile":1.99,
               "fiscalMonth":7,
               "totalMiles": 25,
               "fiscalYear":2019,
               "fiscalWeek":4,
               "offset": 0,
               "totalCost": 3000.837565
            }
         ]
      }
   ]
};

const shipmentsSummaryDailyMock = {
   "status":200,
   "message":"success",
   "data":[
      {
         "title":"Week 1 - July 2019",
         "units":"days",
         "shipmentData":[
            {
               "label":"July 1",
               "shipments":60,
               "costPerMile":1.99,
               "totalMiles": 25,
               "fiscalMonth":7,
               "fiscalYear":2019,
               "fiscalWeek":1,
               "offset": 0,
               "totalCost": 3000.837565
            },
            {
               "label":"July 2",
               "shipments":10,
               "costPerMile":1.10,
               "totalMiles": 25,
               "fiscalMonth":7,
               "fiscalYear":2019,
               "fiscalWeek":1,
               "offset": 0,
               "totalCost": 3000.837565
            },
            {
               "label":"July 3",
               "shipments":20,
               "costPerMile":2.99,
               "totalMiles": 25,
               "fiscalMonth":7,
               "fiscalYear":2019,
               "fiscalWeek":1,
               "offset": 0,
               "totalCost": 3000.837565
            },
            {
               "label":"July 4",
               "shipments":40,
               "costPerMile":1.20,
               "totalMiles": 25,
               "fiscalMonth":7,
               "fiscalYear":2019,
               "fiscalWeek":1,
               "offset": 0,
               "totalCost": 3000.837565
            },
            {
               "label":"July 5",
               "shipments":55,
               "costPerMile":3.68,
               "totalMiles": 25,
               "fiscalMonth":7,
               "fiscalYear":2019,
               "fiscalWeek":1,
               "offset": 0,
               "totalCost": 3000.837565
            },
            {
               "label":"July 6",
               "shipments":20,
               "costPerMile":1.99,
               "totalMiles": 25,
               "fiscalMonth":7,
               "fiscalYear":2019,
               "fiscalWeek":1,
               "offset": 0,
               "totalCost": 3000.837565
            },
            {
               "label":"July 7",
               "shipments":52,
               "costPerMile":2.20,
               "totalMiles": 25,
               "fiscalMonth":7,
               "fiscalYear":2019,
               "fiscalWeek":1,
               "offset": 0,
               "totalCost": 3000.837565
            }
         ]
      }
   ]
};

const getShipmentsSummaryMock = (resultType) => {
  let mockData = [];
  if(resultType === SHIPMENT_RESULT_BY_MONTH){
    mockData = shipmentsSummaryMonthlyMock;
  }
  if(resultType === SHIPMENT_RESULT_BY_WEEK){
    mockData = shipmentsSummaryWeeklyMock;
  }
  if(resultType === SHIPMENT_RESULT_BY_DAY){
    mockData = shipmentsSummaryDailyMock;
  }
  return mockData;
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
    if(response && response.status === 403){
       throw new Error(STATUS_403_ERROR_MESSAGE + apiMessage(response));
    }
    if(response && response.status === 500){
     throw new Error(STATUS_500_ERROR_MESSAGE + apiMessage(response));
    }
    if(response && response.status === 504){
     throw new Error(STATUS_504_ERROR_MESSAGE + apiMessage(response));
    }
};

const consumeApi = async (endPoint, method, JWT, mockData, mockOverride) =>{
  let response = null;
  let json = null;
  if ( MOCK_ALL_DATA || mockOverride){
    console.log(`MOCK_ALL_DATA active for endpoint: ${endPoint}`);
    json = mockData;
  } else {
    if(isValidSession()){
      try{
        // Only call to api if we we have a valid session.
        response = await fetch(endPoint, buildRequestData(method, JWT));
        handleStatus(response);
        json = await response.json();
      } catch (e) {
        // Invalid JWT so it will try to renew the token.
        renewToken();
      }
    } else {
      // Invalid session so it will try to renew the token.
      renewToken();
    }
  }
  if(json && json.data){
    response = json.data;
  }
  return response;
};

export const findLoadByIdApi = async (idLoad, JWT) => {
    const END_POINT = BASE_END_POINT + LOAD_END_POINT + '/' + idLoad;
    return consumeApi(END_POINT, METHOD_GET, JWT, loadByIdMock, false);
};

export const loadPipeLineSummaryApi = async (JWT) => {
    const END_POINT = BASE_END_POINT + LOAD_PIPELINE_SUMMARY_END_POINT ;
    return consumeApi(END_POINT, METHOD_GET, JWT, summaryMock, false);
};

export const getMenuApi = async (JWT, menuTypePosition) => {
    const MENU_TYPE_POSITION = menuTypePosition || "";
    const END_POINT = BASE_END_POINT + MENU_END_POINT + "?type=" + MENU_TYPE_POSITION ;
    return consumeApi(END_POINT, METHOD_GET, JWT, menuMockData, false);
};

export const loadStopsSummaryApi = async (JWT) => {
    const END_POINT = BASE_END_POINT + LOAD_STOP_SUMMARY_END_POINT ;
    return consumeApi(END_POINT, METHOD_GET, JWT, summaryStopMock, false);
};

export const loadShipmentSummaryApi = async (offset, units, fiscalMonth, fiscalYear, fiscalWeek, JWT) => {
    const END_POINT =
      BASE_END_POINT +
      LOAD_SHIPMENT_SUMMARY_END_POINT +
      `?offset=${offset}&units=${units}&fiscalMonth=${fiscalMonth}&fiscalYear=${fiscalYear}&fiscalWeek=${fiscalWeek}`;
    return consumeApi(END_POINT, METHOD_GET, JWT, getShipmentsSummaryMock(units), false);
};
