import { connect } from 'react-redux';
import SideBar from '../components/SideBar';
import { loadMenu } from '../redux/actions/menu';
import { getMenu, getDefaultMenu } from '../redux/reducers/menu';

const mapStateToProps = state => {
  return {
    menu: getMenu(state),
    defaultMenu: getDefaultMenu(state),
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
)(SideBar);

