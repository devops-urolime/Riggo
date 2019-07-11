import { connect } from 'react-redux';
import { withRouter } from 'react-router-dom';
import LoadPage from '../components/LoadPage';
import { findLoadById } from '../redux/actions/load';
import { getCurrentLoad } from '../redux/reducers/load';

const mapStateToProps = state => {
  return {
    load: getCurrentLoad(state),
  }
};

const mapDispatchToProps = dispatch => {
  return {
    findLoad: (loadId) => {
      dispatch(findLoadById(loadId));
    }
  };
};

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(withRouter(LoadPage));
