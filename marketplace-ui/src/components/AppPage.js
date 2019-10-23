import React from 'react';
import PropTypes from 'prop-types';
import Grid from '@material-ui/core/Grid/Grid';
import TopBar from './TopBar';
import SideBarSection from '../containers/SideBarSectionContainer';
import Hidden from '@material-ui/core/Hidden';
import { Switch, withRouter, Route } from 'react-router-dom';
import MainContent from './MainContent';
import { APP_PATH_DASHBOARD, APP_PATH_ROOT } from '../config';
import DashboardPage from '../containers/DashboardPageContainer';

const AppPage = ({
   isLogin,
   setOpenMenu,
  }) => {
  const TopBarWrapper = ({positionAppBar}) => <TopBar positionAppBar={positionAppBar} title="Dashboard" onMenuClick={() => setOpenMenu(true)} isLogin={isLogin}/>;
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
     <SideBarSection />
     <Grid
       container
       spacing={0}
       justify="flex-end"
     >
        <Grid item xs={12}>
           <TopBarWrapper positionAppBar="fixed"/>
        </Grid>
       <Hidden mdDown implementation="js">
        <Grid item xs={10}>
          <MainContentWrapper/>
        </Grid>
       </Hidden>
       <Hidden mdUp implementation="js">
         <Grid item xs={12}>
           <MainContentWrapper/>
         </Grid>
        </Hidden>
     </Grid>
   </div>
  );
};

AppPage.propTypes = {
  setOpenMenu: PropTypes.func,
  isLogin: PropTypes.bool,
};

export default withRouter(AppPage);
