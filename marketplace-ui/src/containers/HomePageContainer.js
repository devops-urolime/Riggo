import { connect } from 'react-redux';
import { withRouter } from 'react-router-dom';
import HomePage from '../components/HomePage';
import { loadPipeLineSummary } from '../redux/actions/load';
import { getPipeLineSummary } from '../redux/reducers/load';

const mapStateToProps = state => {
  return {
    pipeLineSummary: getPipeLineSummary(state),
  }
};

const mapDispatchToProps = dispatch => {
  return {
    loadPipeLineSummary: () => {
      dispatch(loadPipeLineSummary());
    }
  };
};

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(withRouter(HomePage));
