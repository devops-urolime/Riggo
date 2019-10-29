import React, { Component } from 'react';
import Drawer from '@material-ui/core/Drawer';
import Divider from '@material-ui/core/Divider';
import SwipeableDrawer from '@material-ui/core/SwipeableDrawer';
import PropTypes from 'prop-types';
import MenuContent from './MenuContent';
import './SideBar.scss';
import Icon, { MENU_ICON } from './Icon';

class SideBar extends Component {
  render(){
    const {isOpen, handleClose, onClickMenuItem, menu, defaultMenu, variant, currentMenu, swipe} = this.props;
    const ContentWrapper = () =>
      <>
        <div className="SideBar-menu__Header" onClick={handleClose} >
          <Icon name={MENU_ICON}/>
        </div>
        <Divider />
        <MenuContent
          menu={menu}
          defaultMenu={defaultMenu}
          onClickMenuItem={(menuItem)=>{
            handleClose();
            onClickMenuItem(menuItem);
          }}
          currentMenu={currentMenu}
        />
      </>;
    return (swipe) ? (
          <Drawer
            variant={variant}
            anchor="left"
            open={isOpen}
            className="SideBar-menu"
            onClose={handleClose}
          >
           <ContentWrapper />
          </Drawer>
      ):(
      <SwipeableDrawer
         anchor="left"
         open={isOpen}
         className="SideBar-menu SideBar-menu--transparent"
         onClose={handleClose}
         onOpen={()=>{}}
      >
        <ContentWrapper />
      </SwipeableDrawer>
    );
  };
}

SideBar.propTypes = {
  isOpen: PropTypes.bool,
  handleClose: PropTypes.func,
  onClickMenuItem: PropTypes.func,
  menu: PropTypes.array,
  defaultMenu: PropTypes.object,
  currentMenu: PropTypes.object,
  variant: PropTypes.string,
};

export default SideBar;
