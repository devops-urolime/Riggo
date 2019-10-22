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
  onClickMenuItem(menuItem){
    this.props.saveCurrentMenu(menuItem);
  }
  openMenu(){
    this.setState({openMenu:true});
  }
  componentDidMount() {
    this.props.loadMenu();
  }
  render(){
    const { menu, defaultMenu, isLogin, currentMenu} = this.props;
    const {openMenu} = this.state;
    const TopBarWrapper = ({positionAppBar}) => <TopBar positionAppBar={positionAppBar} title="Dashboard" onMenuClick={() => this.openMenu()} isLogin={isLogin}/>;
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
       <Hidden xsDown implementation="js">
         <aside>
           <SideBar
                    menu={menu}
                    defaultMenu={defaultMenu}
                    isOpen={true}
                    handleClose={()=> this.closeMenu()}
                    onClickMenuItem={(item) => this.onClickMenuItem(item)}
                    variant="permanent"
                    currentMenu={currentMenu}
                  />
         </aside>
       </Hidden>
         <Grid
           container
           spacing={0}
           justify="flex-end"
         >
            <Grid item xs={12}>
               <TopBarWrapper positionAppBar="fixed"/>
            </Grid>
            <Grid item xs={12}>
              <MainContentWrapper/>
            </Grid>
         </Grid>
     </div>
    );
  }
}

AppPage.propTypes = {
  loadMenu: PropTypes.func,
  saveCurrentMenu: PropTypes.func,
  menu: PropTypes.array,
  defaultMenu: PropTypes.object,
  currentMenu: PropTypes.object,
  isLogin: PropTypes.bool,
};

export default withRouter(AppPage);
