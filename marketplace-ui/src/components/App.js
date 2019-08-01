import React  from 'react';
import PropTypes from 'prop-types';
import { Switch, Route, withRouter } from 'react-router-dom';
import 'reset-css';
import './App.scss';
import {
  APP_PATH_AUTH0_CALLBACK,
  APP_PATH_LOGIN,
  APP_PATH_ROOT
} from '../config';
import Login from './Login';
import AppPage from '../containers/AppPageContainer';
import Callback from '../containers/CallbackContainer';
import { handleAuthentication } from '../lib/auth';

const App = ({isLogin}) => {
   return (
     <Switch>
       <Route exact path={APP_PATH_ROOT} render={() => {
         return (isLogin) ? <AppPage isLogin={isLogin}/> : <Login />;
       }} />
       <Route exact path={APP_PATH_LOGIN} render={() => (
         <Login />
       )} />
       <Route path={APP_PATH_AUTH0_CALLBACK} render={(props) => {
          handleAuthentication();
          return <Callback {...props} />;
       }}/>
     </Switch>
   );
};

App.propTypes = {
  isLogin: PropTypes.bool,
};

export default withRouter(App);
