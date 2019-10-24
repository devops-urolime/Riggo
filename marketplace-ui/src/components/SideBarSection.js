import React, { Component } from 'react';
import PropTypes from 'prop-types';
import Hidden from '@material-ui/core/Hidden';
import SideBar from './SideBar';

class SideBarSection extends Component {

  componentDidMount() {
      this.props.loadMenu();
  }

  closeMenu(){
    this.props.setOpenMenu(false);
  }

  onClickMenuItem(menuItem){
    this.props.saveCurrentMenu(menuItem);
  }

  openMenu(){
    this.props.setOpenMenu(true);
  }

  render() {
    const { menu, defaultMenu, currentMenu, openMenu} = this.props;
    return(
      <>
        <Hidden smDown implementation="js">
           <SideBar
              menu={menu}
              defaultMenu={defaultMenu}
              isOpen={true}
              handleClose={()=> this.closeMenu()}
              onClickMenuItem={(item) => this.onClickMenuItem(item)}
              variant="permanent"
              currentMenu={currentMenu}
              swipe={true}
            />
         </Hidden>
         <Hidden mdUp implementation="js">
           {
             openMenu &&
             <SideBar
               menu={menu}
               defaultMenu={defaultMenu}
               isOpen={true}
               handleClose={()=> this.closeMenu()}
               onClickMenuItem={(item) => this.onClickMenuItem(item)}
               variant="permanent"
               currentMenu={currentMenu}
               swipe={false}
             />
           }
         </Hidden>
      </>
    );
  }
}

SideBarSection.propTypes = {
  loadMenu: PropTypes.func,
  saveCurrentMenu: PropTypes.func,
  setOpenMenu: PropTypes.func,
  menu: PropTypes.array,
  defaultMenu: PropTypes.object,
  currentMenu: PropTypes.object,
  isLogin: PropTypes.bool,
  openMenu: PropTypes.bool,
};

export default SideBarSection;
