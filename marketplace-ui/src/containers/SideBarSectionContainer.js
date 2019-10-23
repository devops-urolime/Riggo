import { connect } from 'react-redux';
import SideBarSection from '../components/SideBarSection';
import { loadMenu, setCurrentMenu, setOpenMenu } from '../redux/actions/menu';
import { getMenu, getDefaultMenu, getCurrentMenu, getOpenMenu } from '../redux/reducers/menu';
import { MENU_TYPE_POSITION_LEFT } from '../config';

const mapStateToProps = state => {
  return {
    menu: getMenu(state),
    defaultMenu: getDefaultMenu(state),
    currentMenu: getCurrentMenu(state),
    openMenu: getOpenMenu(state),
  }
};

const mapDispatchToProps = dispatch => {
  return {
    loadMenu: () => {
      dispatch(loadMenu(MENU_TYPE_POSITION_LEFT));
    },
    saveCurrentMenu: (menuItem) => {
      dispatch(setCurrentMenu(menuItem));
    },
    setOpenMenu: (openMenu) => {
      dispatch(setOpenMenu(openMenu));
    }
  };
};

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(SideBarSection);

