import React from 'react';
import PropTypes from 'prop-types';
import { makeStyles } from '@material-ui/core/styles';
import AppBar from '@material-ui/core/AppBar';
import Toolbar from '@material-ui/core/Toolbar';
import Typography from '@material-ui/core/Typography';
import IconButton from '@material-ui/core/IconButton';
import Icon, { EXIT_ICON, MENU_ICON, USER_ACCOUNT_ICON } from './Icon';
import './TopBar.scss';
import { withRouter } from 'react-router-dom';
import { APP_PATH_LOGIN } from '../config';

const useStyles = makeStyles(theme => ({
  root: {
    flexGrow: 1,
  },
  menuButton: {
    marginRight: theme.spacing(2),
  },
  title: {
    flexGrow: 1,
  },
}));
const MenuAppBar = ({isLogin, login, logout, title, onMenuClick}) => {
  const classes = useStyles();
  return (
    <div className={classes.root}>
      <AppBar className="TopBar" position="static">
        <Toolbar>
          <IconButton onClick={onMenuClick} edge="start" className={classes.menuButton} color="inherit" aria-label="Menu">
            <Icon name={MENU_ICON}/>
          </IconButton>
          <Typography variant="h6" className={classes.title}>
            {title}
          </Typography>
          {isLogin &&
            <IconButton onClick={()=>logout()} edge="end" className={classes.menuButton} color="inherit" aria-label="Menu">
              <Icon name={EXIT_ICON}/>
            </IconButton>
          }
          {!isLogin &&
            <IconButton onClick={()=>login()} edge="end" className={classes.menuButton} color="inherit" aria-label="Menu">
              <Icon name={USER_ACCOUNT_ICON}/>
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

const TopBar = ({title, onMenuClick, auth, history}) => {
  const isLogin =  auth.isAuthenticated();
  const login = () => {
    history.push(APP_PATH_LOGIN);
  };
  return (
    <MenuAppBar
      title={title}
      onMenuClick={onMenuClick}
      logout={auth.logout}
      login={login}
      isLogin={isLogin}
    />
  );
};

TopBar.propTypes = {
  title: PropTypes.string,
  onMenuClick: PropTypes.func,
  auth: PropTypes.object,
  history: PropTypes.object,
};

export default withRouter(TopBar);
