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

const App = ({auth, isLogin}) => {
   return (
     <Switch>
       <Route exact path={APP_PATH_ROOT} render={() => {
         return (isLogin) ? <AppPage auth={auth}/> : <Login auth={auth}/>;
       }} />
       <Route exact path={APP_PATH_LOGIN} render={() => (
         <Login auth={auth}/>
       )} />
       <Route path={APP_PATH_AUTH0_CALLBACK} render={(props) => {
          auth.handleAuthentication();
          return <Callback auth={auth} {...props} />;
       }}/>
     </Switch>
   );
};

App.propTypes = {
  auth: PropTypes.object,
  isLogin: PropTypes.bool,
};

export default withRouter(App);
