import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { withRouter } from 'react-router-dom';
import CardSummary from './CardSummary';
import './DashboardPage.scss';
import Grid from '@material-ui/core/Grid';
import TitleSection from './TitleSection';
import Paper from '@material-ui/core/Paper';
import Grow from '@material-ui/core/Grow';
import PieVisualization, {
  DARK2,
  NIVO
} from './PieVisualization';
import BarVisualization, { BAR_DARK2 } from './BarVisualization';
import MultiYAxesVisualization from './MultiYAxesVisualization';
import { SHIPMENT_RESULT_BY_DAY, SHIPMENT_RESULT_BY_MONTH, SHIPMENT_RESULT_BY_WEEK } from '../api';

const ROOT_INDEX_BAR_VISUALIZATION = "status";
const KEYS_DATA_BAR_VISUALIZATION= [
    "Quoted",
    "Booked",
    "Dispatched",
    "@Pickup",
    "In transit",
    "@Delivery",
    "Pending Docs",
    "Docs Received",
    "Invoiced"
];

const PICKUP_ROOT_PROP = "Pickup";
const DELIVERY_ROOT_PROP = "Delivery";

const digestDataToBarVisualization = (data) => {
  const COLOR_SUB_FIX = "Color";
  return data.map( item => {
    const setOfData = { status: item.name.toString() };
    item.subStatuses.forEach( (subItem) => {
        setOfData[subItem.name.toString() ] = subItem.count;
        setOfData[subItem.name.toString() + COLOR_SUB_FIX] = "hsl(9, 87%, 67%)";
    });
    return setOfData;
  });
};

const digestDataToCardVisualization = (data) => {
  return data.map((item) => {
    return {
      number: item.count,
      label: item.name
    };
  });
};

const digestDataToPieVisualization = (data, rootDataProp) => {
  let result = [];
  const dataToDigest = data.filter(item => item.name === rootDataProp);
  const isData = (dataToDigest.length > 0 && dataToDigest[0]);
  const reducerTotal = (accumulator, item) => {
    return accumulator + item.count;
  };
  const percentItem = (item, totalAmount) => {
    let percent = 0;
    if (item.count > 0) {
      percent = (item.count * 100) / totalAmount;
    }
    return percent.toFixed(2);
  };
  if (isData){
    const data = dataToDigest[0].data;
    const totalAmount =  data.reduce(reducerTotal, 0);
    result = data.map((item) => {
     return {
       id: item.name,
       label: item.name,
       value:  item.count,
       percent: percentItem(item, totalAmount)
     };
    });
  }
  return result;
};

const digestDataToMultiYAxes = (data) => {
  return (data && data.length > 0 ) ?
    data.map((item) => {
      const dataToVisualize = item.shipmentData.map((axeInfo) =>{
         return {
           date: axeInfo.label,
           "Shipment Delivered": axeInfo.shipments,
           "Cost Per Mile": axeInfo.costPerMile,
         };
      });
      return {
        title: item.title,
        data: dataToVisualize
      };
    }) : [] ;
};

const getTimingEffect = (index) => {
  const GROW_TIMING_EFFECT = [ 600, 800, 1000 ];
  return ( index < GROW_TIMING_EFFECT.length ) ? GROW_TIMING_EFFECT[index] : 500;
};

const getConfigGrow = (index) => {
  const configGrow = {
    in:true
  };
  if (index > 0){
    configGrow['timeout'] = getTimingEffect(index);
    configGrow['style'] = { transformOrigin: '0 0 0' };
  }
  return configGrow;
};

const SHIPMENT_OFFSET_DEFAULT = 0;
const SHIPMENT_FISCAL_MONTH_DEFAULT = 7;
const SHIPMENT_FISCAL_YEAR_DEFAULT = 2019;
const SHIPMENT_FISCAL_WEEK_DEFAULT = 1;

const VIEW_TYPES = [
  SHIPMENT_RESULT_BY_MONTH,
  SHIPMENT_RESULT_BY_WEEK,
  SHIPMENT_RESULT_BY_DAY
];

class DashboardPage extends Component {

  constructor(props) {
    super(props);
    this.state = {
      isBarGroupMode: true,
      viewTypeShipment: SHIPMENT_RESULT_BY_MONTH,
      offsetShipment: SHIPMENT_RESULT_BY_MONTH,
      fiscalMonthShipment: SHIPMENT_FISCAL_MONTH_DEFAULT,
      fiscalYearShipment: SHIPMENT_FISCAL_YEAR_DEFAULT,
      weekShipment: SHIPMENT_FISCAL_WEEK_DEFAULT,
      navViewMonthData:null,
      navViewWeekData:null,
      navViewDayData:null,
    };
  }

  componentDidMount() {
    this.props.loadPipeLineSummary();
    this.props.loadStopSummary();
    this.loadShipments();
  }

  loadShipments = () =>{
    this.props.loadShipmentSummary(
      this.state.offsetShipment ,
      this.state.viewTypeShipment,
      this.state.fiscalMonthShipment,
      this.state.fiscalYearShipment,
      this.state.weekShipment
    );
  };

  toggleBarGroup = ()=> {
    this.setState(prevState => ({
      isBarGroupMode: !prevState.isBarGroupMode
    }));
  };

  nextViewType = (current, type) => {
    let idx = type.indexOf(current);
    if (idx === type.length - 1) {
      return type[0];
    }
    return type[idx + 1];
  };

  prevViewType = (current, type) =>{
    let idx = type.indexOf(current);
    if (idx === 0) {
      return type[type.length - 1];
    }
    return type[idx - 1];
  };

  navigateToNextViewType = (item) => {
    const { viewTypeShipment } = this.state;
    const nextView = this.nextViewType(viewTypeShipment, VIEW_TYPES);
    this.setState({
      viewTypeShipment: nextView
    });
    this.updateNavigation(item.payload, viewTypeShipment);
    this.loadShipments(
      item.payload.offset,
      item.payload.units,
      item.payload.fiscalMonth,
      item.payload.fiscalYear,
      item.payload.week
    );
  };

  navigateToPevViewType = (item) => {
    const { viewTypeShipment } = this.state;
    const prevView = this.prevViewType(viewTypeShipment, VIEW_TYPES);
    this.setState({
      viewTypeShipment: prevView
    });
    this.updateNavigation(item.payload, viewTypeShipment);
    this.loadShipments(
      item.payload.offset,
      item.payload.units,
      item.payload.fiscalMonth,
      item.payload.fiscalYear,
      item.payload.week
    );
  };

  updateNavigation = (payload, viewType) =>{
    switch (viewType) {
      case SHIPMENT_RESULT_BY_MONTH:
        this.setState({
          navViewMonthData: payload
         });
        break;
      case SHIPMENT_RESULT_BY_WEEK:
        this.setState({
          navViewWeekData: payload
         });
        break;
      case SHIPMENT_RESULT_BY_DAY:
        this.setState({
          navViewDayData: payload
         });
        break;
    }
  };

  render(){
    const { pipeLineSummary, stopSummary, shipmentSummary } = this.props;
    const pipeLineSummaryBar = digestDataToBarVisualization(pipeLineSummary);
    const pipeLineSummaryCard = digestDataToCardVisualization(pipeLineSummary);
    const stopSummaryPickUpPie = digestDataToPieVisualization(stopSummary, PICKUP_ROOT_PROP);
    const stopSummaryDeliveryPie = digestDataToPieVisualization(stopSummary, DELIVERY_ROOT_PROP);
    const shipmentSummaryMultiYAxes = digestDataToMultiYAxes(shipmentSummary);
    const { isBarGroupMode } = this.state;
      return (
        <Grid
          container
          spacing={0}
          direction="row"
          alignItems="center"
          className="DashboardPage"
        >
          <Grid
            container
            spacing={0}
            direction="row"
            justify="flex-start"
            alignItems="flex-start"
          >
            <Grid item xs={12}>
              <TitleSection label="Status"/>
            </Grid>
          </Grid>
          <Grid
            spacing={0}
            container
            direction="row"
            justify="center"
            alignItems="center"
            className="CardSummarySection"
          >
          { pipeLineSummaryCard &&
            pipeLineSummaryCard.map((item, index) => {
            const configGrow =  getConfigGrow(index) ;
            return (
              <Grow {...configGrow}  key={`card-summary-grow-${index}`}>
                <Grid xs={4} key={`card-summary-${index}`} item>
                    <CardSummary number={item.number} label={item.label}/>
                </Grid>
              </Grow>
            );
          })}
          </Grid>
          <Grid
            container
            spacing={0}
            direction="column"
            alignItems="center"
          >
            <Grid item xs={11}>
              <Paper className="DashboardPage__MuiPaper-root" onClick={() => this.toggleBarGroup()}>
                <BarVisualization
                  data={pipeLineSummaryBar}
                  colorsScheme={BAR_DARK2}
                  rootClass="StatusVisualization"
                  groupMode={isBarGroupMode}
                  indexBy={ROOT_INDEX_BAR_VISUALIZATION}
                  keys={KEYS_DATA_BAR_VISUALIZATION}
                />
              </Paper>
            </Grid>
          </Grid>
          <Grid
            container
            spacing={0}
            direction="row"
            justify="flex-start"
            alignItems="flex-start"
          >
            <Grid item xs={12}>
              <TitleSection label="Shipments"/>
            </Grid>
          </Grid>
          <Grid
            container
            spacing={0}
            direction="column"
            alignItems="center"
            className="DashboardPage"
          >
            <Grid item xs={11}>
              <Paper className="DashboardPage__MuiPaper-root">
                {
                  shipmentSummaryMultiYAxes && shipmentSummaryMultiYAxes.length > 0 &&
                  <MultiYAxesVisualization
                    title={shipmentSummaryMultiYAxes[0].title}
                    data={shipmentSummaryMultiYAxes[0].data}
                    onClickBar={this.navigateToNextViewType}
                    rootClass="ShipmentsVisualization"
                  />
                }
              </Paper>
            </Grid>
          </Grid>
          <Grid
            container
            spacing={0}
            direction="row"
            justify="flex-start"
            alignItems="flex-start"
          >
            <Grid item xs={12}>
              <TitleSection label="On Time Performance - Pickup"/>
            </Grid>
          </Grid>
          <Grid
            container
            spacing={0}
            direction="column"
            alignItems="center"
            className="DashboardPage"
          >
            <Grid item xs={11}>
              <Paper className="DashboardPage__MuiPaper-root">
                <PieVisualization
                  data={stopSummaryPickUpPie}
                  rootClass="PerformancePickUpVisualization"
                  colorsScheme={NIVO}
                />
              </Paper>
            </Grid>
          </Grid>
          <Grid
            container
            spacing={0}
            direction="row"
            justify="flex-start"
            alignItems="flex-start"
          >
            <Grid item xs={12}>
              <TitleSection label="On Time Performance - Delivery"/>
            </Grid>
          </Grid>
          <Grid
            container
            spacing={0}
            direction="column"
            alignItems="center"
            className="DashboardPage"
          >
            <Grid item xs={11}>
              <Paper className="DashboardPage__MuiPaper-root">
                <PieVisualization
                  data={stopSummaryDeliveryPie}
                  rootClass="PerformancePickUpVisualization"
                  colorsScheme={DARK2}
                />
              </Paper>
            </Grid>
          </Grid>
        </Grid>
      );
  }
}

DashboardPage.propTypes = {
  pipeLineSummary: PropTypes.array,
  stopSummary: PropTypes.array,
  shipmentSummary: PropTypes.array,
  loadPipeLineSummary: PropTypes.func,
  loadStopSummary: PropTypes.func,
  loadShipmentSummary: PropTypes.func,
};

DashboardPage.defaultProps = {
  pipeLineSummary: [],
  stopSummary: [],
  shipmentSummary:[],
};
export default withRouter(DashboardPage);
