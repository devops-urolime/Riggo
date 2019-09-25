import { connect } from 'react-redux';
import AppPage from '../components/AppPage';
import { loadMenu, setCurrentMenu } from '../redux/actions/menu';
import { getMenu, getDefaultMenu, getCurrentMenu } from '../redux/reducers/menu';
import { isLogin } from '../redux/reducers/auth';
import { MENU_TYPE_POSITION_LEFT } from '../config';

const mapStateToProps = state => {
  return {
    menu: getMenu(state),
    defaultMenu: getDefaultMenu(state),
    currentMenu: getCurrentMenu(state),
    isLogin: isLogin(state)
  }
};

const mapDispatchToProps = dispatch => {
  return {
    loadMenu: () => {
      dispatch(loadMenu(MENU_TYPE_POSITION_LEFT));
    },
    saveCurrentMenu: (menuItem) => {
      dispatch(setCurrentMenu(menuItem));
    }
  };
};

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(AppPage);

