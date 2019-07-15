import { connect } from 'react-redux';
import MenuContent from '../components/MenuContent';
import { loadMenu } from '../redux/actions/menu';
import { getMenu } from '../redux/reducers/menu';

const mapStateToProps = state => {
  return {
    menu: getMenu(state),
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
)(MenuContent);

