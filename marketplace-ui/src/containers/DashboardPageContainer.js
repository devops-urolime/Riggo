import { connect } from 'react-redux';
import { withRouter } from 'react-router-dom';
import DashboardPage from '../components/DashboardPage';
import { loadPipeLineSummary, loadShipmentSummary, loadStopSummary } from '../redux/actions/load';
import { getPipeLineSummary, getShipmentSummary, getStopSummary } from '../redux/reducers/load';

const mapStateToProps = state => {
  return {
    pipeLineSummary: getPipeLineSummary(state),
    stopSummary: getStopSummary(state),
    shipmentSummary: getShipmentSummary(state),
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
    loadShipmentSummary: (offset, units, fiscalMonth, fiscalYear, week) => {
      dispatch(loadShipmentSummary(offset, units, fiscalMonth, fiscalYear, week));
    },
  };
};

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(withRouter(DashboardPage));
