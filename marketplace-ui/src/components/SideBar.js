import React, { Component } from 'react';
import Drawer from '@material-ui/core/Drawer';
import Divider from '@material-ui/core/Divider';
import IconButton from '@material-ui/core/IconButton';
import ChevronLeftIcon from '@material-ui/icons/ChevronLeft';
import PropTypes from 'prop-types';
import MenuContent from './MenuContent';
import { withRouter } from 'react-router-dom';
import './SideBar.css';
import Icon from './Icon';

class SideBar extends Component {
  componentDidMount() {
      this.props.loadMenu();
  }
  render(){
    const {isOpen, handleClose, menu, defaultMenu, history} = this.props;
    if(defaultMenu && defaultMenu.url){
      history.push(defaultMenu.url);
    }
    return (
        <div>
          <Drawer
            variant="persistent"
            anchor="left"
            open={isOpen}
            className="SideBar-menu"
          >
            <nav className="SideBar-menu__Header">
              <Icon name="Logo"/>
              <IconButton className="SideBar-menu__IconButton-Back" onClick={handleClose}>
                 <ChevronLeftIcon />
              </IconButton>
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
  loadMenu: PropTypes.func,
  defaultMenu: PropTypes.object,
  history: PropTypes.object,
};

export default withRouter(SideBar);
