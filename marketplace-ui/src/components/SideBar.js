import React from 'react';
import { makeStyles, useTheme } from '@material-ui/core/styles';
import Drawer from '@material-ui/core/Drawer';
import List from '@material-ui/core/List';
import Divider from '@material-ui/core/Divider';
import IconButton from '@material-ui/core/IconButton';
import ChevronLeftIcon from '@material-ui/icons/ChevronLeft';
import ChevronRightIcon from '@material-ui/icons/ChevronRight';
import ListItem from '@material-ui/core/ListItem';
import ListItemIcon from '@material-ui/core/ListItemIcon';
import ListItemText from '@material-ui/core/ListItemText';
import PropTypes from 'prop-types';
import Callback from './Callback';

const drawerWidth = 240;

const useStyles = makeStyles(theme => ({
  drawer: {
    width: drawerWidth,
    flexShrink: 0,
  },
  drawerPaper: {
    width: drawerWidth,
  },
  drawerHeader: {
    display: 'flex',
    alignItems: 'center',
    padding: '0 8px',
    ...theme.mixins.toolbar,
    justifyContent: 'flex-end',
  },
}));

const SideBar = ({isOpen, handleClose}) => {
  const classes = useStyles();
  const theme = useTheme();
  return (
    <div>
      <Drawer
        className={classes.drawer}
        variant="persistent"
        anchor="left"
        open={isOpen}
        classes={{
          paper: classes.drawerPaper,
        }}
      >
        <div className={classes.drawerHeader}>
          <IconButton onClick={handleClose}>
            {theme.direction === 'ltr' ? <ChevronLeftIcon /> : <ChevronRightIcon />}
          </IconButton>
        </div>
        <Divider />
        <List>
          <ListItem button>
            <ListItemIcon>
              <svg width="14" height="13" viewBox="0 0 14 13" fill="none" xmlns="http://www.w3.org/2000/svg">
                <rect width="6" height="7.2" fill="black"/>
                <rect x="8" y="5.80005" width="6" height="7.2" fill="black"/>
                <rect x="8" width="6" height="4" fill="black"/>
                <rect y="9" width="6" height="4" fill="black"/>
              </svg>
            </ListItemIcon>
            <ListItemText primary='Dashboard' />
          </ListItem>
          <ListItem button>
            <ListItemIcon>
              <svg width="24" height="16" viewBox="0 0 24 16" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path d="M7.10973 11.1477C5.94228 11.1477 4.99541 12.0945 4.99541 13.2585C4.99541 14.4259 5.94228 15.3728 7.10973 15.3728C8.27377 15.3728 9.22059 14.4259 9.22059 13.2585C9.22059 12.0945 8.27377 11.1477 7.10973 11.1477ZM7.10973 14.3173C6.526 14.3173 6.0509 13.8422 6.0509 13.2585C6.0509 12.6782 6.526 12.2031 7.10973 12.2031C7.69 12.2031 8.16514 12.6782 8.16514 13.2585C8.16514 13.8422 7.69004 14.3173 7.10973 14.3173ZM5.28973 11.1486H0.557961C0.370549 11.1486 0.218627 11.3005 0.218627 11.4879V12.5175C0.218627 12.7049 0.370549 12.8569 0.557961 12.8569H4.35067C4.44973 12.1775 4.79263 11.5773 5.28973 11.1486ZM18.1561 11.1477C16.992 11.1477 16.0453 12.0945 16.0453 13.2585C16.0453 14.4259 16.9921 15.3728 18.1561 15.3728C19.3235 15.3728 20.2669 14.4259 20.2669 13.2585C20.267 12.0945 19.3235 11.1477 18.1561 11.1477ZM18.1561 14.3173C17.5724 14.3173 17.1007 13.8422 17.1007 13.2585C17.1007 12.6782 17.5724 12.2031 18.1561 12.2031C18.7398 12.2031 19.2115 12.6782 19.2115 13.2585C19.2116 13.8422 18.7398 14.3173 18.1561 14.3173ZM23.6607 11.1477H22.836V6.53566C22.836 5.9961 22.6731 5.46668 22.3711 5.01869L20.3044 1.95763C19.802 1.211 18.9571 0.763045 18.0544 0.763045H14.8507C14.4774 0.763045 14.172 1.06508 14.172 1.44179V11.1477H8.92875C9.42424 11.5786 9.76698 12.176 9.86541 12.858H15.4005C15.5939 11.5108 16.7545 10.4689 18.1562 10.4689C19.5577 10.4689 20.7184 11.5108 20.9152 12.858H23.6607C23.8507 12.858 24 12.7053 24 12.5187V11.487C24 11.3004 23.8507 11.1477 23.6607 11.1477ZM20.2805 5.62955H16.052C15.8654 5.62955 15.7127 5.48021 15.7127 5.29017V2.9452C15.7127 2.75853 15.8655 2.60583 16.052 2.60583H18.6278C18.7398 2.60583 18.8451 2.6601 18.9062 2.74834L20.5589 5.09672C20.7183 5.32072 20.5554 5.62955 20.2805 5.62955ZM12.5565 10.4689H0.678706C0.303882 10.4689 0 10.1651 0 9.79021V6.7359C0 6.36104 0.303882 6.05715 0.678706 6.05715H12.5565C12.9314 6.05715 13.2353 6.36104 13.2353 6.7359V9.79021C13.2353 10.1651 12.9314 10.4689 12.5565 10.4689ZM5.42988 5.03908H0.678706C0.303882 5.03908 0 4.73516 0 4.36033V1.30606C0 0.931201 0.303882 0.627319 0.678706 0.627319H5.42984C5.80471 0.627319 6.10859 0.931201 6.10859 1.30606V4.36033C6.10863 4.73516 5.80475 5.03908 5.42988 5.03908ZM12.5565 5.03908H7.80545C7.43059 5.03908 7.12671 4.7352 7.12671 4.36037V1.30606C7.12671 0.931201 7.43059 0.627359 7.80545 0.627359H12.5566C12.9315 0.627359 13.2353 0.93124 13.2353 1.30606V4.36037C13.2353 4.73516 12.9314 5.03908 12.5565 5.03908Z" fill="black"/>
              </svg>
            </ListItemIcon>
            <ListItemText primary='Shipments' />
          </ListItem>
          <ListItem button>
            <ListItemIcon>
              <svg width="18" height="18" viewBox="0 0 18 18" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path d="M9.21496 12.4297C7.47344 12.359 6.00357 11.1199 5.64633 9.40811C5.59423 9.16624 5.56819 8.91692 5.56446 8.6676L5.55702 8.09081L0.142676 6.78467L0.0533675 7.62566C-0.0508261 8.60433 -0.00245054 9.59045 0.198494 10.5542C1.16228 15.1499 5.41934 18.339 10.0969 17.9706L10.9379 17.9036L9.79547 12.4521L9.21496 12.4297ZM1.48231 8.6341L2.29353 8.83133L1.54185 9.58301C1.50464 9.26671 1.48231 8.95041 1.48231 8.6341ZM1.70186 10.4724L3.14196 9.03599L3.97179 9.23694L1.94002 11.2687C1.84699 11.0082 1.76884 10.744 1.70186 10.4724ZM2.60611 12.679C2.47587 12.4632 2.35679 12.2399 2.24888 12.0092L4.26577 9.99234C4.33647 10.2565 4.42578 10.5096 4.53369 10.7552L2.60611 12.679ZM3.02661 13.3079L4.89465 11.4399C5.02489 11.6483 5.17002 11.8492 5.33003 12.0353L3.48803 13.8736C3.32802 13.6949 3.17173 13.5052 3.02661 13.3079ZM4.009 14.4057L5.84355 12.5711C6.02589 12.7349 6.2194 12.8837 6.42406 13.0214L4.56718 14.8783C4.37368 14.7294 4.18762 14.5694 4.009 14.4057ZM5.18862 15.3062L7.09388 13.401C7.33204 13.5163 7.58136 13.6093 7.83812 13.6875L5.851 15.6746C5.62028 15.563 5.40073 15.4402 5.18862 15.3062ZM6.57291 15.9984L8.59352 13.9777L8.77214 14.8299L7.35064 16.2514C7.09016 16.1807 6.82967 16.0951 6.57291 15.9984ZM8.22512 16.43L8.9582 15.6969L9.12937 16.5119C8.82424 16.5007 8.52282 16.471 8.22512 16.43Z" fill="black"/>
                <path d="M17.8146 6.8554C16.9141 2.56114 12.9622 -0.307904 8.70142 0.0270038L9.55357 4.08311C11.5853 4.16498 13.3827 5.61252 13.8181 7.69268C14.2534 9.77283 13.1929 11.8195 11.3621 12.7126L12.2142 16.7724C16.2517 15.3658 18.7152 11.1497 17.8146 6.8554Z" fill="black"/>
                <path d="M7.37293 4.54085L6.52078 0.484741C4.07223 1.3369 2.20419 3.22727 1.31482 5.53813L5.34488 6.50936C5.7877 5.66837 6.49101 4.97251 7.37293 4.54085Z" fill="black"/>
              </svg>
            </ListItemIcon>
            <ListItemText primary='Reports' />
          </ListItem>
          <ListItem button>
            <ListItemIcon>
              <svg width="21" height="21" viewBox="0 0 21 21" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path d="M14.5272 7.64429H17.9879V8.79785H14.5272V7.64429Z" fill="black"/>
                <path d="M14.5272 13.6428C14.5272 15.1194 14.9656 16.5381 15.7732 17.7726L16.2576 18.4878L16.7419 17.7726C17.5494 16.5383 17.9879 15.1194 17.9879 13.6428V9.95142H14.5272V13.6428Z" fill="black"/>
                <path d="M14.5272 4.18359H17.9879V6.49072H14.5272V4.18359Z" fill="black"/>
                <path d="M19.1415 5.33716V6.49072H17.9879V7.64429H19.1415V9.95142H20.3335V5.33716H19.1415Z" fill="black"/>
                <path d="M11.6433 0.645996C10.2475 0.645996 9.08252 1.63806 8.81708 2.95312H0.749817C0.680723 2.76855 0.645996 2.58398 0.645996 2.37634C0.645996 1.41876 1.41876 0.645996 2.37634 0.645996H11.6433Z" fill="black"/>
                <path d="M11.6433 1.79956C10.6878 1.79956 9.91296 2.57437 9.91296 3.52991V4.10669H1.83801V20.3335H13.3737C13.3737 19.9058 13.3737 2.49482 13.3737 3.52991C13.3737 2.57437 12.5988 1.79956 11.6433 1.79956ZM7.60583 9.87451C6.64826 9.87451 5.87549 9.10162 5.87549 8.14417C5.87549 7.39435 6.35987 6.74835 7.02905 6.51764V5.29871H8.18262V6.51764C8.8518 6.74835 9.33618 7.39435 9.33618 8.14417H8.18262C8.18262 7.82105 7.92895 7.56738 7.60583 7.56738C7.28272 7.56738 7.02905 7.82105 7.02905 8.14417C7.02905 8.46716 7.28272 8.72095 7.60583 8.72095C8.56341 8.72095 9.33618 9.49372 9.33618 10.4513C9.33618 11.2011 8.8518 11.8471 8.18262 12.0778V13.2968H7.02905V12.0778C6.35987 11.8471 5.87549 11.2011 5.87549 10.4513H7.02905C7.02905 10.7743 7.28272 11.0281 7.60583 11.0281C7.92895 11.0281 8.18262 10.7743 8.18262 10.4513C8.18262 10.1282 7.92895 9.87451 7.60583 9.87451ZM9.91296 17.9495H5.29871V16.7959H9.91296V17.9495ZM11.0665 15.6423H4.14514V14.4888H11.0665V15.6423Z" fill="black"/>
              </svg>
            </ListItemIcon>
            <ListItemText primary='Billing' />
          </ListItem>
        </List>

      </Drawer>
    </div>
  );
};


Callback.propTypes = {
  isOpen: PropTypes.bool,
  handleClose: PropTypes.func,
};
export default SideBar;
