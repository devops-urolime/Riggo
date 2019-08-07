import React from 'react';
import PropTypes from 'prop-types';
import ListItem from '@material-ui/core/ListItem/ListItem';
import ListItemIcon from '@material-ui/core/ListItemIcon/ListItemIcon';
import ListItemText from '@material-ui/core/ListItemText/ListItemText';
import List from '@material-ui/core/List/List';
import { withRouter } from 'react-router-dom';
import Icon from './Icon';
import './MenuContent.scss';
import { APP_PATH_DASHBOARD, URL_CODE_APP } from '../config';

const translateUrlCode = (urlCode) => {
  const url = URL_CODE_APP[urlCode];
  return (url) ? url : APP_PATH_DASHBOARD;
};

const MenuContent = ({ menu, history, onClick }) => {
  return (
      <List className="MenuContent">
        {
          menu &&
          menu.map((menuItem, idx) => {
            const {name, url } = menuItem;
            return (
              <ListItem onClick={()=> {
                history.push(translateUrlCode(url));
                onClick();
              }} key={`menu-item-custom-${idx}`} button>
                <ListItemIcon>
                  <Icon name={name}/>
                </ListItemIcon>
                <ListItemText primary={name} />
              </ListItem>
            );
          })
        }
      </List>
    );
};

MenuContent.propTypes = {
  menu: PropTypes.array,
  history: PropTypes.object,
  defaultMenu: PropTypes.object,
  onClick: PropTypes.func,
};

export default withRouter(MenuContent);
