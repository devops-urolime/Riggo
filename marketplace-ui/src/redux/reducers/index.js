import { combineReducers } from 'redux';
import load from './load';
import auth from './auth';

const rootReducer = combineReducers({
  load,
  auth
});

export default rootReducer;
