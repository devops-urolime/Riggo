import { takeEvery, all, put } from 'redux-saga/effects';
import {
  APP_API_CALL_FAIL,
  GET_LOAD,
  GET_LOAD_PIPE_LINE_SUMMARY, GET_LOAD_PIPE_LINE_SUMMARY_FAIL,
  GET_LOAD_PIPE_LINE_SUMMARY_SUCCESS,
  GET_LOAD_SUCCESS
} from '../actions/load';
import { findLoadByIdApi, loadPipeLineSummaryApi } from '../../api';

function* getLoadSaga(action) {
    const result = yield findLoadByIdApi(action.id);
    yield put({type: GET_LOAD_SUCCESS, load: result});
}

function* getLoadPipeLineSummarySaga() {
    try{
      const result = yield loadPipeLineSummaryApi();
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

export function* watchGetLoad() {
    yield takeEvery(GET_LOAD, getLoadSaga);
}

export function* watchGetLoadPipeLineSummary() {
    yield takeEvery(GET_LOAD_PIPE_LINE_SUMMARY, getLoadPipeLineSummarySaga);
}

export default function* rootSaga() {
    yield all([
      watchGetLoad(),
      watchGetLoadPipeLineSummary()
    ]);
}
