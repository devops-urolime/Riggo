import React, { Component } from 'react';
import Drawer from '@material-ui/core/Drawer';
import Divider from '@material-ui/core/Divider';
import PropTypes from 'prop-types';
import MenuContent from './MenuContent';
import './SideBar.scss';
import Icon, { LOGO_ICON } from './Icon';

class SideBar extends Component {
  render(){
    const {isOpen, handleClose, menu, defaultMenu} = this.props;
    return (
        <div>
          <Drawer
            variant="persistent"
            anchor="left"
            open={isOpen}
            className="SideBar-menu"
          >
            <nav className="SideBar-menu__Header" onClick={handleClose}>
              <Icon name={LOGO_ICON}/>
            </nav>
            <Divider />
            <MenuContent menu={menu} defaultMenu={defaultMenu}/>
          </Drawer>
        </div>
      );
  };
}

SideBar.propTypes = {
  isOpen: PropTypes.bool,
  handleClose: PropTypes.func,
  menu: PropTypes.array,
  defaultMenu: PropTypes.object,
};

export default SideBar;
