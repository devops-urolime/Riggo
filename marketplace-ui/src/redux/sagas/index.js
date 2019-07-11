import { takeEvery, all, put } from 'redux-saga/effects';
import {GET_LOAD, GET_LOAD_SUCCESS} from "../actions/load";
import { findLoadByIdApi } from '../../api';

function* getLoadSaga(action) {
    const result = yield findLoadByIdApi(action.id);
    yield put({type: GET_LOAD_SUCCESS, load: result});
}


export function* watchGetLoad() {
    yield takeEvery(GET_LOAD, getLoadSaga);
}

export default function* rootSaga() {
    yield all([
      watchGetLoad()
    ]);
}
