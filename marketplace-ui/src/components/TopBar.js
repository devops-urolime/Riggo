import React from 'react';
import PropTypes from 'prop-types';
import { makeStyles } from '@material-ui/core/styles';
import AppBar from '@material-ui/core/AppBar';
import Toolbar from '@material-ui/core/Toolbar';
import Typography from '@material-ui/core/Typography';
import IconButton from '@material-ui/core/IconButton';
import Icon from '@material-ui/core/Icon';
import './TopBar.scss';
import { withRouter } from 'react-router-dom';
import { APP_PATH_LOGIN } from '../config';
import { logout } from '../lib/auth';

const useStyles = makeStyles(theme => ({
  root: {
    flexGrow: 8,
  },
  menuButton: {
    marginRight: theme.spacing(2),
  },
  title: {
    flexGrow: 1,
  },
}));
const MenuAppBar = ({isLogin, login, logout, title, onMenuClick, positionAppBar}) => {
  const classes = useStyles();
  return (
    <div className={classes.root}>
      <AppBar className="TopBar" position={positionAppBar}>
        <Toolbar>
          {
            isLogin &&
           <IconButton onClick={onMenuClick} edge="start" className={classes.menuButton}
                       color="inherit" aria-label="Menu">
             <Icon>menu</Icon>
           </IconButton>
          }
          <Typography variant="h6" className={classes.title}>
            {isLogin && title}
          </Typography>
          {isLogin &&
            <IconButton onClick={()=>logout()} edge="end" className={classes.menuButton} color="inherit" aria-label="Menu">
              <Icon>exit</Icon>
            </IconButton>
          }
          {!isLogin &&
            <IconButton onClick={()=>login()} edge="end" className={classes.menuButton} color="inherit" aria-label="Menu">
              <Icon>user</Icon>
            </IconButton>
          }
        </Toolbar>
      </AppBar>
    </div>
  );
};

MenuAppBar.propTypes = {
  isLogin: PropTypes.bool,
  login: PropTypes.func,
  logout: PropTypes.func,
  title: PropTypes.string,
  onMenuClick: PropTypes.func,
};

const TopBar = ({title, onMenuClick, isLogin, history, positionAppBar}) => {
  const login = () => {
    history.push(APP_PATH_LOGIN);
  };
  return (
    <MenuAppBar
      title={title}
      onMenuClick={onMenuClick}
      logout={logout}
      login={login}
      isLogin={isLogin}
      positionAppBar={positionAppBar}
    />
  );
};

TopBar.propTypes = {
  title: PropTypes.string,
  positionAppBar: PropTypes.string,
  onMenuClick: PropTypes.func,
  isLogin: PropTypes.bool,
  history: PropTypes.object,
};

export default withRouter(TopBar);
