import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { withRouter } from 'react-router-dom';
import CardSummary from './CardSummary';
import './DashboardPage.scss';
import Grid from '@material-ui/core/Grid';
import TitleSection from './TitleSection';
import Grow from '@material-ui/core/Grow';
import PieVisualization, {
  DARK2,
  NIVO
} from './PieVisualization';
import MultiYAxesVisualization from './MultiYAxesVisualization';
import { SHIPMENT_RESULT_BY_DAY, SHIPMENT_RESULT_BY_MONTH, SHIPMENT_RESULT_BY_WEEK } from '../api';
import TotalSummary from './TotalSummary';
import LineDivider, { VERTICAL_LINE } from './LineDivider';
import Section from './Section';
import StackVisualization from './StackVisualization';

const PICKUP_ROOT_PROP = "Pickup";
const DELIVERY_ROOT_PROP = "Delivery";
const NO_STATUS = "No Status";

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
    result = data.filter((itemData) => itemData.name !== NO_STATUS).map((item) => {
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
  let result = [];
  if(data && data.length > 0 ) {
    const reducerTotalCostInPeriod = (accumulator, item) => {
      return accumulator + item.totalCost;
    };
    const reducerTotalMilesInPeriod = (accumulator, item) => {
      return accumulator + item.totalMiles;
    };
    const reducerTotalShipmentsInPeriod = (accumulator, item) => {
      return accumulator + item.shipments;
    };
    result = data.map((item) => {
          const { shipmentData, units } =  item;
          const totalShipmentsInPeriod =  shipmentData.reduce(reducerTotalShipmentsInPeriod, 0);
          const totalCostInPeriod =  shipmentData.reduce(reducerTotalCostInPeriod, 0);
          const totalMilesInPeriod =  shipmentData.reduce(reducerTotalMilesInPeriod, 0);
          const totalCostPerMileInPeriod = (totalMilesInPeriod > 0 ) ?
                totalCostInPeriod / totalMilesInPeriod : 0;
          const dataToVisualize = shipmentData.map((axeInfo) =>{
             return {
               date: axeInfo.label,
               "Shipment Delivered": axeInfo.shipments,
               "Cost Per Mile": axeInfo.costPerMile,
               "fiscalMonth": axeInfo.fiscalMonth,
               "fiscalYear": axeInfo.fiscalYear,
               "fiscalWeek": axeInfo.fiscalWeek,
               "offset": axeInfo.offset,
               "units": units,
               "costPerMile": axeInfo.costPerMile,
               "totalCost": axeInfo.totalCost
             };
          });
          return {
            title: item.title,
            data: dataToVisualize,
            totalCostPerMileInPeriod: totalCostPerMileInPeriod.toFixed(2),
            totalShipmentsInPeriod,
            totalCostInPeriod: totalCostInPeriod.toFixed(2)
          };
        })
  }
  return result;
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
      viewTypeShipment: SHIPMENT_RESULT_BY_MONTH,
      offsetShipment: SHIPMENT_OFFSET_DEFAULT,
      fiscalMonthShipment: SHIPMENT_FISCAL_MONTH_DEFAULT,
      fiscalYearShipment: SHIPMENT_FISCAL_YEAR_DEFAULT,
      weekShipment: SHIPMENT_FISCAL_WEEK_DEFAULT,
      navCursorOffset: SHIPMENT_OFFSET_DEFAULT,
      itemBar: {}
    };
  }

  componentDidMount() {
    this.props.loadPipeLineSummary();
    this.props.loadStopSummary();
    this.props.loadShipmentSummary(
      this.state.offsetShipment ,
      this.state.viewTypeShipment,
      this.state.fiscalMonthShipment,
      this.state.fiscalYearShipment,
      this.state.weekShipment
    );
  }

  nextViewType = (current, type) => {
    let idx = type.indexOf(current);
    const lastViewTypeIndex = type.length - 1;
    if (idx === lastViewTypeIndex) {
      return type[0];
    }
    return type[idx + 1];
  };

  prevViewType = (current, type) =>{
    let idx = type.indexOf(current);
    return (idx !== 0) ? type[idx - 1]: type[0];
  };

  updateViewType = (item) =>{
    const { viewTypeShipment } = this.state;
    const nextView = this.nextViewType(viewTypeShipment, VIEW_TYPES);
    this.setState({
      viewTypeShipment: nextView,
      itemBar: item.payload
    });
    if(nextView !== viewTypeShipment){
      this.props.loadShipmentSummary(
        item.payload.offset,
        nextView,
        item.payload.fiscalMonth,
        item.payload.fiscalYear,
        item.payload.fiscalWeek
      );
    }
  };

  goToPrevViewType = () =>{
    const { viewTypeShipment, itemBar } = this.state;
    const prevView = this.prevViewType(viewTypeShipment, VIEW_TYPES);
    this.setState({
      viewTypeShipment: prevView,
    });
    if(prevView !== viewTypeShipment){
      this.props.loadShipmentSummary(
        itemBar.offset,
        prevView,
        itemBar.fiscalMonth,
        itemBar.fiscalYear,
        itemBar.fiscalWeek
      );
    }
  };

  navigateToPrevOffset = () => {
    this.setState(prevState => {
      const prevOffset =  prevState.navCursorOffset + 1;
      this.props.loadShipmentSummary(
        prevOffset ,
        this.state.viewTypeShipment,
        this.state.fiscalMonthShipment,
        this.state.fiscalYearShipment,
        this.state.weekShipment
      );
      return ({
        navCursorOffset: prevOffset,
      });
    });
  };

  navigateToNextOffset = () => {
    this.setState(prevState => {
      const nextOffset =  prevState.navCursorOffset - 1;
      this.props.loadShipmentSummary(
        nextOffset,
        this.state.viewTypeShipment,
        this.state.fiscalMonthShipment,
        this.state.fiscalYearShipment,
        this.state.weekShipment
      );
      return ({
        navCursorOffset: nextOffset,
      });
    });
  };

  render(){
    const { pipeLineSummary, stopSummary, shipmentSummary } = this.props;
    const { viewTypeShipment } = this.state;
    const pipeLineSummaryCard = pipeLineSummary && digestDataToCardVisualization(pipeLineSummary);
    const stopSummaryPickUpPie = stopSummary && digestDataToPieVisualization(stopSummary, PICKUP_ROOT_PROP);
    const stopSummaryDeliveryPie = stopSummary && digestDataToPieVisualization(stopSummary, DELIVERY_ROOT_PROP);
    const shipmentSummaryMultiYAxes = digestDataToMultiYAxes(shipmentSummary);
    const isShipmentData = shipmentSummaryMultiYAxes && shipmentSummaryMultiYAxes.length > 0;
    const isNavigation = (viewTypeShipment === SHIPMENT_RESULT_BY_MONTH);
      return (
        <Grid
          container
          spacing={0}
          direction="row"
          alignItems="center"
          className="DashboardPage"
        >
          <Section
            top={ () => <TitleSection label="Status"/> }
            content={ ()=>
            <>
              { pipeLineSummaryCard &&
                pipeLineSummaryCard.map((item, index) => {
                const configGrow =  getConfigGrow(index) ;
                return (
                  <Grid xs={4} key={`card-summary-${index}`} item>
                    <Grow {...configGrow}  key={`card-summary-grow-${index}`}>
                      <CardSummary number={item.number} label={item.label}/>
                    </Grow>
                  </Grid>
                );
              })}
              <Grid xs={12} item>
                <StackVisualization
                  data={pipeLineSummary}
                />
              </Grid>
            </>
            }
          />
          <Section
            top={() => <TitleSection label="Shipments"/>}
            content={ () => {
              return isShipmentData &&
               shipmentSummaryMultiYAxes.map((summaryItem, idx) =>{
                 return(
                   <Grid item xs={12} key={`shipment-viz-${idx}`}>
                     <Grid
                       container
                       spacing={0}
                       direction="row"
                       alignItems="center"
                       justify="center"
                     >
                       <Grid xs={8} item>
                         <Grid
                            container
                            spacing={0}
                            direction="row"
                            alignItems="center"
                            justify="center"
                          >
                           </Grid>
                            <MultiYAxesVisualization
                             title={summaryItem.title}
                             data={summaryItem.data}
                             onClickBar={this.updateViewType}
                             onClickBack={this.navigateToPrevOffset}
                             onClickNext={this.navigateToNextOffset}
                             rootClass="ShipmentsVisualization"
                             showNext={isNavigation}
                             showPrev={isNavigation}
                            />
                        </Grid>
                        <Grid xs={1} item>
                          <LineDivider orientation={VERTICAL_LINE}/>
                        </Grid>
                        <Grid xs={3} item>
                            <TotalSummary
                            title="Total Shipments In Period"
                            legend={`${summaryItem.totalShipmentsInPeriod}`}
                            />
                            <TotalSummary
                            title="Total Cost In Period"
                            legend={`$${summaryItem.totalCostInPeriod}`}
                            />
                            <TotalSummary
                            title="Cost/ml In Period"
                            legend={`$${summaryItem.totalCostPerMileInPeriod}`}
                            />
                        </Grid>
                    </Grid>
                  </Grid>
                )
               });
              }
            }
          />
          <Grid item xs={6}>
            <Section
              top={() =>  <TitleSection label="On Time Performance - Pickup"/> }
              content={()=>
                <Grid item xs={12}>
                  <PieVisualization
                    data={stopSummaryPickUpPie}
                    rootClass="PerformancePickUpVisualization"
                    colorsScheme={NIVO}
                  />
                </Grid>
              }
            />
          </Grid>
          <Grid item xs={6}>
            <Section
              top={()=> <TitleSection label="On Time Performance - Delivery"/> }
              content={() =>
                <Grid item xs={12}>
                  <PieVisualization
                    data={stopSummaryDeliveryPie}
                    rootClass="PerformancePickUpVisualization"
                    colorsScheme={DARK2}
                  />
                </Grid>
              }
            />
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
