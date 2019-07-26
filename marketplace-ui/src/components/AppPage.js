import React, { Component } from 'react';
import PropTypes from 'prop-types';
import Grid from '@material-ui/core/Grid/Grid';
import TopBar from './TopBar';
import SideBar from '../components/SideBar';
import { Switch, withRouter, Route } from 'react-router-dom';
import MainContent from './MainContent';
import { APP_PATH_HOME, APP_PATH_ROOT } from '../config';
import HomePage from '../containers/HomePageContainer';

class AppPage extends Component{

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
    const { auth:{renewSession}, loadMenu } = this.props;
    if (localStorage.getItem('isLoggedIn') === 'true') {
      renewSession();
    }
    loadMenu();
  }
  render(){
    const {auth, menu, defaultMenu} = this.props;
    const {openMenu} = this.state;
    return (
     <div className="App-layout">
         <Grid container spacing={0}>
            <Grid item xs={12}>
             <TopBar title="Dashboard" onMenuClick={() => this.openMenu()} auth={auth}/>
            </Grid>
            <Grid item xs={12}>
             <SideBar
               menu={menu}
               defaultMenu={defaultMenu}
               isOpen={openMenu}
               handleClose={()=> this.closeMenu()}
             />
            </Grid>
            <Grid item xs={12}>
              <MainContent>
                <Switch>
                  <Route exact path={APP_PATH_ROOT} render={() => (<HomePage auth={auth}/>)} />
                  <Route exact path={APP_PATH_HOME} render={() => (<HomePage auth={auth}/>)} />
                </Switch>
              </MainContent>
            </Grid>
         </Grid>
     </div>
    );
  }
}

AppPage.propTypes = {
  auth: PropTypes.object,
  loadMenu: PropTypes.func,
  menu: PropTypes.array,
  defaultMenu: PropTypes.object,
};

export default withRouter(AppPage);
