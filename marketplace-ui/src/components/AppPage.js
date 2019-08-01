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
    const {loadMenu } = this.props;
    loadMenu();
  }
  render(){
    const { menu, defaultMenu, isLogin} = this.props;
    const {openMenu} = this.state;
    return (
     <div className="App-layout">
         <Grid container spacing={0}>
            <Grid item xs={12}>
             <TopBar title="Dashboard" onMenuClick={() => this.openMenu()} isLogin={isLogin}/>
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
                {
                  isLogin &&
                 <Switch>
                   <Route exact path={APP_PATH_ROOT} render={() => (<HomePage />)} />
                   <Route exact path={APP_PATH_HOME} render={() => (<HomePage />)} />
                 </Switch>
                }
              </MainContent>
            </Grid>
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
