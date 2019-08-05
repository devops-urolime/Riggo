import { connect } from 'react-redux';
import { withRouter } from 'react-router-dom';
import DashboardPage from '../components/DashboardPage';
import { loadPipeLineSummary, loadStopSummary } from '../redux/actions/load';
import { getPipeLineSummary, getStopSummary } from '../redux/reducers/load';

const mapStateToProps = state => {
  return {
    pipeLineSummary: getPipeLineSummary(state),
    stopSummary: getStopSummary(state),
  }
};

const mapDispatchToProps = dispatch => {
  return {
    loadPipeLineSummary: () => {
      dispatch(loadPipeLineSummary());
    },
    loadStopSummary: () => {
      dispatch(loadStopSummary());
    },
  };
};

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(withRouter(DashboardPage));
