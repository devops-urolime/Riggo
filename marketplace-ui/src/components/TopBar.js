import React from 'react';
import PropTypes from 'prop-types';
import { makeStyles } from '@material-ui/core/styles';
import AppBar from '@material-ui/core/AppBar';
import Toolbar from '@material-ui/core/Toolbar';
import Typography from '@material-ui/core/Typography';
import IconButton from '@material-ui/core/IconButton';

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
const MenuAppBar = ({title, onMenuClick}) => {
  const classes = useStyles();
  return (
    <div className={classes.root}>
      <AppBar position="static">
        <Toolbar>
          <IconButton onClick={onMenuClick} edge="start" className={classes.menuButton} color="inherit" aria-label="Menu">
            <svg width="18" height="12" viewBox="0 0 18 12" fill="none" xmlns="http://www.w3.org/2000/svg">
              <path fillRule="evenodd" clipRule="evenodd" d="M0 12H18V10H0V12ZM0 7H11V5H0V7ZM0 0V2H18V0H0Z" fill="white"/>
            </svg>
          </IconButton>
          <Typography variant="h6" className={classes.title}>
            {title}
          </Typography>
        </Toolbar>
      </AppBar>
    </div>
  );
};

const TopBar = ({title, onMenuClick}) => {
  return (
    <MenuAppBar title={title} onMenuClick={onMenuClick}/>
  );
};

TopBar.propTypes = {
  title: PropTypes.string,
  onMenuClick: PropTypes.func,
};

export default TopBar;
