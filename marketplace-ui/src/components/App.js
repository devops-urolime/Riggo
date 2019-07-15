import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { Provider } from "react-redux";
import { BrowserRouter, Switch, Route } from 'react-router-dom';
import configureStore from "../redux/store/configureStore";
import 'reset-css';
import './App.css';
import HomePage from "../containers/HomePageContainer";
import LoadPage from "../containers/LoadPageContainer";
import Callback from '../containers/CallbackContainer';
import TopBar from './TopBar';
import MainContent from './MainContent';
import SideBar from '../containers/SideBarContainer';
import Grid from '@material-ui/core/Grid';
import { APP_PATH_AUTH0_CALLBACK, APP_PATH_HOME, APP_PATH_LOAD_ID, APP_PATH_ROOT } from '../config';

const store = configureStore();

class App extends Component {
  constructor(props) {
    super(props);
    this.state = {openMenu: false};
  }
  closeMenu(){
    this.setState({openMenu:false});
  }
  openMenu(){
    this.setState({openMenu:true});
  }
  componentDidMount() {
    const { renewSession } = this.props.auth;
    if (localStorage.getItem('isLoggedIn') === 'true') {
      renewSession();
    }
  }
  render(){
    const {auth} = this.props;
    const {openMenu} = this.state;
    return (
      <BrowserRouter>
      <div className="App-layout">
        <Provider store={store}>
          <Grid container spacing={0}>
            <Grid item xs={12}>
              <TopBar title="Dashboard" onMenuClick={() => this.openMenu()}/>
            </Grid>
            <Grid item xs={12}>
              <SideBar isOpen={openMenu} handleClose={()=> this.closeMenu()}/>
            </Grid>
            <Grid item xs={12}>
              <MainContent>
                  <Switch>
                      <Route exact path={APP_PATH_ROOT} render={() => (<HomePage auth={auth}/>)} />
                      <Route exact path={APP_PATH_HOME} render={() => (<HomePage auth={auth}/>)} />
                      <Route path={APP_PATH_LOAD_ID} component={(props) => (
                        <LoadPage auth={auth} loadId={props.match.params.id} {...props} />
                      )} />
                      <Route path={APP_PATH_AUTH0_CALLBACK} render={(props) => {
                        auth.handleAuthentication();
                        return <Callback auth={auth} {...props} />;
                      }}/>
                  </Switch>
              </MainContent>
            </Grid>
          </Grid>
        </Provider>
      </div>
      </BrowserRouter>
    );
  };
}

App.propTypes = {
  auth: PropTypes.object,
};

export default App;
