import { takeEvery, all, put, select } from 'redux-saga/effects';
import {
  APP_API_CALL_FAIL,
  GET_LOAD,
  GET_LOAD_FAIL,
  GET_LOAD_PIPE_LINE_SUMMARY,
  GET_LOAD_PIPE_LINE_SUMMARY_FAIL,
  GET_LOAD_PIPE_LINE_SUMMARY_SUCCESS,
  GET_LOAD_STOP_SUMMARY,
  GET_LOAD_STOP_SUMMARY_FAIL,
  GET_LOAD_STOP_SUMMARY_SUCCESS,
  GET_LOAD_SUCCESS
} from '../actions/load';
import {
  findLoadByIdApi,
  getMenuApi,
  loadPipeLineSummaryApi,
  loadStopsSummaryApi
} from '../../api';
import { GET_MENU, GET_MENU_FAIL, GET_MENU_SUCCESS, SET_DEFAULT_MENU } from '../actions/menu';
import { getToken } from '../reducers/auth';

const getDefaultMenu = (menuList) => {
   if(menuList){
     return menuList[0];
   } else {
     return null;
   }
};

function* getLoadSaga(action) {
    try{
      const JWT = yield select(getToken);
      const result = yield findLoadByIdApi(action.id, JWT);
      yield put({type: GET_LOAD_SUCCESS, load: result});
    } catch (e) {
      yield put({type: GET_LOAD_FAIL});
      yield put({
        type: APP_API_CALL_FAIL,
        message: `Error when get Load by ID : ${action.id}`,
        err: e
      });
    }
}

function* getLoadPipeLineSummarySaga() {
    try{
      const JWT = yield select(getToken);
      const result = yield loadPipeLineSummaryApi(JWT);
      yield put({type: GET_LOAD_PIPE_LINE_SUMMARY_SUCCESS, pipeLineSummary: result});
    } catch (e) {
      yield put({type: GET_LOAD_PIPE_LINE_SUMMARY_FAIL});
      yield put({
        type: APP_API_CALL_FAIL,
        message: "Error when get Load PipeLine Summary",
        err: e
      });
    }
}

function* getLoadStopSummarySaga() {
    try{
      const JWT = yield select(getToken);
      const result = yield loadStopsSummaryApi(JWT);
      yield put({type: GET_LOAD_STOP_SUMMARY_SUCCESS, stopSummary: result});
    } catch (e) {
      yield put({type: GET_LOAD_STOP_SUMMARY_FAIL});
      yield put({
        type: APP_API_CALL_FAIL,
        message: "Error when get Load Stop Summary",
        err: e
      });
    }
}

function* getMenuSaga() {
    try{
      const JWT = yield select(getToken);
      const result = yield getMenuApi(JWT);
      yield put({type: GET_MENU_SUCCESS, menu: result});
      yield put({type: SET_DEFAULT_MENU, menuItem: getDefaultMenu(result)});
    } catch (e) {
      yield put({type: GET_MENU_FAIL});
      yield put({
        type: APP_API_CALL_FAIL,
        message: "Error when get Menu",
        err: e
      });
    }
}

export function* watchGetLoad() {
    yield takeEvery(GET_LOAD, getLoadSaga);
}

export function* watchGetLoadStopSummary() {
    yield takeEvery(GET_LOAD_STOP_SUMMARY, getLoadStopSummarySaga);
}

export function* watchGetLoadPipeLineSummary() {
    yield takeEvery(GET_LOAD_PIPE_LINE_SUMMARY, getLoadPipeLineSummarySaga);
}

export function* watchGetMenu() {
    yield takeEvery(GET_MENU, getMenuSaga);
}

export default function* rootSaga() {
    yield all([
      watchGetLoad(),
      watchGetLoadPipeLineSummary(),
      watchGetLoadStopSummary(),
      watchGetMenu()
    ]);
}
