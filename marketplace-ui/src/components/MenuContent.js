import React from 'react';
import PropTypes from 'prop-types';
import ListItem from '@material-ui/core/ListItem/ListItem';
import ListItemIcon from '@material-ui/core/ListItemIcon/ListItemIcon';
import ListItemText from '@material-ui/core/ListItemText/ListItemText';
import List from '@material-ui/core/List/List';
import { withRouter } from 'react-router-dom';
import Icon from './Icon';

const MenuContent = ({ menu, history }) => {
  return (
      <List>
        {
          menu &&
          menu.map((menuItem, idx) => {
            const {name, url } = menuItem;
            return (
              <ListItem onClick={()=> history.push(url)} key={`menu-item-custom-${idx}`} button>
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
};

export default withRouter(MenuContent);
