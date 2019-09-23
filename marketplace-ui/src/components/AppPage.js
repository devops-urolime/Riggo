import React, { Component } from 'react';
import PropTypes from 'prop-types';
import Grid from '@material-ui/core/Grid/Grid';
import TopBar from './TopBar';
import SideBar from '../components/SideBar';
import Hidden from '@material-ui/core/Hidden';
import { Switch, withRouter, Route } from 'react-router-dom';
import MainContent from './MainContent';
import { APP_PATH_DASHBOARD, APP_PATH_ROOT } from '../config';
import DashboardPage from '../containers/DashboardPageContainer';

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
    const {loadMenu } = this.props;
    loadMenu();
  }
  render(){
    const { menu, defaultMenu, isLogin} = this.props;
    const {openMenu} = this.state;
    const TopBarWrapper = () => <TopBar positionAppBar="static" title="Dashboard" onMenuClick={() => this.openMenu()} isLogin={isLogin}/>;
    const MainContentWrapper = () =>
        <MainContent>
          {
            isLogin &&
           <Switch>
             <Route exact path={APP_PATH_ROOT} render={() => (<DashboardPage />)} />
             <Route exact path={APP_PATH_DASHBOARD} render={() => (<DashboardPage />)} />
           </Switch>
          }
        </MainContent>
    ;
    return (
     <div className="App-layout">
         <Grid container spacing={0}>
            <Hidden xsDown implementation="js">
              <Grid item xs={2}>
              </Grid>
              <Grid item xs={10}>
                 <TopBarWrapper />
              </Grid>
            </Hidden>
            <Hidden smUp implementation="js">
              <Grid item xs={12}>
                 <SideBar
                   menu={menu}
                   defaultMenu={defaultMenu}
                   isOpen={openMenu}
                   handleClose={()=> this.closeMenu()}
                   variant="persistent"
                 />
              </Grid>
            </Hidden>
           <Hidden smDown implementation="js">
             <Grid item xs={2}>
                <SideBar
                  menu={menu}
                  defaultMenu={defaultMenu}
                  isOpen={true}
                  handleClose={()=> this.closeMenu()}
                  variant="permanent"
                />
             </Grid>
           </Hidden>
           <Hidden xsDown implementation="js">
            <Grid item xs={10}>
              <MainContentWrapper/>
            </Grid>
           </Hidden>
         </Grid>
     </div>
    );
  }
}

AppPage.propTypes = {
  loadMenu: PropTypes.func,
  menu: PropTypes.array,
  defaultMenu: PropTypes.object,
  isLogin: PropTypes.bool,
};

export default withRouter(AppPage);
