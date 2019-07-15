import { combineReducers } from 'redux';
import load from './load';
import auth from './auth';
import menu from './menu';

const rootReducer = combineReducers({
  load,
  auth,
  menu
});

export default rootReducer;
