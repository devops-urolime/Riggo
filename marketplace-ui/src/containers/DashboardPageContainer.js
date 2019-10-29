import { connect } from 'react-redux';
import { withRouter } from 'react-router-dom';
import DashboardPage from '../components/DashboardPage';
import { loadPipeLineSummary, loadShipmentSummary, loadStopSummary } from '../redux/actions/load';
import {
  getItemVizBar,
  getNavCursorOffset,
  getPipeLineSummary,
  getShipmentSummary,
  getStopSummary,
  getViewTypeShipment
} from '../redux/reducers/load';

const mapStateToProps = state => {
  return {
    pipeLineSummary: getPipeLineSummary(state),
    stopSummary: getStopSummary(state),
    shipmentSummary: getShipmentSummary(state),
    viewTypeShipment: getViewTypeShipment(state),
    itemVizBar: getItemVizBar(state),
    navCursorOffset: getNavCursorOffset(state),
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
    loadShipmentSummary: (
      offset,
      units,
      fiscalMonth,
      fiscalYear,
      week,
      viewTypeShipment,
      itemVizBar,
      navCursorOffset
      ) => {
      dispatch(loadShipmentSummary(
        offset,
        units,
        fiscalMonth,
        fiscalYear,
        week,
        viewTypeShipment,
        itemVizBar,
        navCursorOffset
      ));
    },
  };
};

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(withRouter(DashboardPage));
