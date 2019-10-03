import React, { Component }  from 'react';
import PropTypes from 'prop-types';
import { Switch, Route, BrowserRouter } from 'react-router-dom';
import 'reset-css';
import './App.scss';
import {
  APP_PATH_AUTH0_CALLBACK,
  APP_PATH_BILLING,
  APP_PATH_DASHBOARD,
  APP_PATH_FORGOT_PASSWORD,
  APP_PATH_LOAD_ID,
  APP_PATH_LOGIN,
  APP_PATH_REPORTS,
  APP_PATH_ROOT,
  APP_PATH_SHIPMENTS
} from '../config';
import Login from './Login';
import AppPage from '../containers/AppPageContainer';
import Callback from '../containers/CallbackContainer';
import { handleAuthentication } from '../lib/auth';
import ForgotPasswordPage from './ForgotPasswordPage';

const LoadApp = ({isLogin}) => {
  return (isLogin) ? <AppPage isLogin={isLogin}/> : <Login />;
};

class App extends Component {

  componentDidMount() {
    this.props.isAlreadyLocallyAuthenticated();
  }

  render () {
    const { isLogin } = this.props;
    return (
      <BrowserRouter>
      <Switch>
        <Route exact path={APP_PATH_FORGOT_PASSWORD} render={() => <ForgotPasswordPage />} />
        <Route exact path={APP_PATH_ROOT} render={() => <LoadApp isLogin={isLogin}/>} />
        <Route exact path={APP_PATH_DASHBOARD} render={() => <LoadApp isLogin={isLogin}/>} />
        <Route exact path={APP_PATH_SHIPMENTS} render={() => <LoadApp isLogin={isLogin}/>} />
        <Route exact path={APP_PATH_REPORTS} render={() => <LoadApp isLogin={isLogin}/>} />
        <Route exact path={APP_PATH_BILLING} render={() => <LoadApp isLogin={isLogin}/>} />
        <Route exact path={APP_PATH_LOAD_ID} render={() => <LoadApp isLogin={isLogin}/>} />
        <Route exact path={APP_PATH_LOGIN} render={() => (
         <Login />
        )} />
        <Route path={APP_PATH_AUTH0_CALLBACK} render={(props) => {
          handleAuthentication();
          return <Callback {...props} />;
        }}/>
      </Switch>
      </BrowserRouter>
    );
  }
}

App.propTypes = {
  isLogin: PropTypes.bool,
  isAlreadyLocallyAuthenticated: PropTypes.func
};

export default App;
