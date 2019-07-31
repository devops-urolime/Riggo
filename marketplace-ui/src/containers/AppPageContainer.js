import { connect } from 'react-redux';
import AppPage from '../components/AppPage';
import { loadMenu } from '../redux/actions/menu';
import { getMenu, getDefaultMenu } from '../redux/reducers/menu';
import { isLogin } from '../redux/reducers/auth';

const mapStateToProps = state => {
  return {
    menu: getMenu(state),
    defaultMenu: getDefaultMenu(state),
    isLogin: isLogin(state)
  }
};

const mapDispatchToProps = dispatch => {
  return {
    loadMenu: () => {
      dispatch(loadMenu());
    }
  };
};

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(AppPage);

