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
import BarVisualization, { BAR_DARK2 } from './BarVisualization';
import MultiYAxesVisualization from './MultiYAxesVisualization';
import { SHIPMENT_RESULT_BY_DAY, SHIPMENT_RESULT_BY_MONTH, SHIPMENT_RESULT_BY_WEEK } from '../api';
import TotalSummary from './TotalSummary';
import LineDivider, { HORIZONTAL_LINE } from './LineDivider';
import Section from './Section';

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
            totalCostPerMileInPeriod,
            totalShipmentsInPeriod,
            totalCostInPeriod
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
      isBarGroupMode: true,
      viewTypeShipment: SHIPMENT_RESULT_BY_MONTH,
      offsetShipment: SHIPMENT_OFFSET_DEFAULT,
      fiscalMonthShipment: SHIPMENT_FISCAL_MONTH_DEFAULT,
      fiscalYearShipment: SHIPMENT_FISCAL_YEAR_DEFAULT,
      weekShipment: SHIPMENT_FISCAL_WEEK_DEFAULT,
      showNext: false,
      showPrev: false,
      historyNav:[],
      historyNavIndex:0
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

  hasNextHistory = (historyNav, historyNavIndex) =>{
    const size = historyNav.length;
    return historyNavIndex < size;
  };

  hasPrevHistory = (historyNav, historyNavIndex) => {
    const size = historyNav.length;
    return historyNavIndex <= size && historyNavIndex !== 0;
  };

  navigateToNextViewType = (item) => {
    const { viewTypeShipment } = this.state;
    if(item.payload){
      const nextView = this.nextViewType(viewTypeShipment, VIEW_TYPES);
      this.setState(prevState => {
        const historyNavUpdate = (
          prevState.historyNavIndex >= prevState.historyNav.length
        ) ? [...prevState.historyNav, { viewTypeShipment, item }] : [...prevState.historyNav];
        const historyNavIndex = prevState.historyNavIndex + 1 ;
        const hasNextHistory = this.hasNextHistory(historyNavUpdate, historyNavIndex);
        const hasPrevHistory = this.hasPrevHistory(historyNavUpdate, historyNavIndex);
        return ({
          viewTypeShipment: nextView,
          historyNav: historyNavUpdate,
          historyNavIndex,
          showNext: hasNextHistory,
          showPrev: hasPrevHistory,
        });
      });
      this.props.loadShipmentSummary(
        item.payload.offset,
        nextView,
        item.payload.fiscalMonth,
        item.payload.fiscalYear,
        item.payload.fiscalWeek
      );
    } else if (this.state.showNext){
      const nextIndexItem = (
        this.state.historyNav.length + 1 >= this.state.historyNavIndex &&
        this.state.historyNavIndex !== (this.state.historyNav.length - 1)
      ) ? this.state.historyNavIndex + 1 : this.state.historyNavIndex;
      const item = this.state.historyNav[ nextIndexItem ];
      if(item){
        const nextItem = item.item;
        this.setState(prevState => {
          const historyNavUpdate = (prevState.historyNavIndex >= prevState.historyNav.length) ? [...prevState.historyNav, { viewTypeShipment, item }] : [...prevState.historyNav];
          const hasNextHistory = this.hasNextHistory(historyNavUpdate, nextIndexItem +1);
          const hasPrevHistory = this.hasPrevHistory(historyNavUpdate, nextIndexItem +1);
          return ({
            viewTypeShipment: nextItem.payload.units,
            historyNavIndex: nextIndexItem,
            showNext: hasNextHistory,
            showPrev: hasPrevHistory,
          });
        });
        this.props.loadShipmentSummary(
          nextItem.payload.offset,
          nextItem.payload.units,
          nextItem.payload.fiscalMonth,
          nextItem.payload.fiscalYear,
          nextItem.payload.fiscalWeek
        );
      }
    }
  };

  navigateToPrevViewType = () => {
    const { historyNav, historyNavIndex } = this.state;
    const hasPrevHistory = this.hasPrevHistory(historyNav, historyNavIndex);
    if(hasPrevHistory){
      const prevIndexItem = historyNavIndex - 1;
      const item = (historyNav[prevIndexItem]) ? historyNav[prevIndexItem].item: {};
      this.setState(prevState => {
        const historyNavUpdate = [...prevState.historyNav];
        const historyNavIndex = (prevState.historyNavIndex > 0 ) ? prevState.historyNavIndex - 1 : prevState.historyNavIndex;
        const hasNextHistory = this.hasNextHistory(historyNavUpdate, historyNavIndex);
        const hasPrevHistory = this.hasPrevHistory(historyNavUpdate, historyNavIndex);
        const prevItem = (
          historyNavUpdate[prevIndexItem] &&
          historyNavUpdate[prevIndexItem].item) ?  historyNavUpdate[prevIndexItem].item : {};
        return ({
          viewTypeShipment: (prevItem.units) ? prevItem.units : SHIPMENT_RESULT_BY_MONTH,
          historyNav: historyNavUpdate,
          historyNavIndex,
          showNext: hasNextHistory,
          showPrev: hasPrevHistory,
        });
      });
      if (item.payload){
        this.props.loadShipmentSummary(
          item.payload.offset,
          item.payload.units,
          item.payload.fiscalMonth,
          item.payload.fiscalYear,
          item.payload.fiscalWeek
        );
      }
    }
  };

  render(){
    const { pipeLineSummary, stopSummary, shipmentSummary } = this.props;
    const pipeLineSummaryBar = digestDataToBarVisualization(pipeLineSummary);
    const pipeLineSummaryCard = digestDataToCardVisualization(pipeLineSummary);
    const stopSummaryPickUpPie = digestDataToPieVisualization(stopSummary, PICKUP_ROOT_PROP);
    const stopSummaryDeliveryPie = digestDataToPieVisualization(stopSummary, DELIVERY_ROOT_PROP);
    const shipmentSummaryMultiYAxes = digestDataToMultiYAxes(shipmentSummary);
    const {
      isBarGroupMode,
      showNext,
      showPrev,
    } = this.state;
    const isShipmentData = shipmentSummaryMultiYAxes && shipmentSummaryMultiYAxes.length > 0;
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
                <BarVisualization
                  onClickRoot={() => this.toggleBarGroup()}
                  data={pipeLineSummaryBar}
                  colorsScheme={BAR_DARK2}
                  rootClass="StatusVisualization"
                  groupMode={isBarGroupMode}
                  indexBy={ROOT_INDEX_BAR_VISUALIZATION}
                  keys={KEYS_DATA_BAR_VISUALIZATION}
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
                     <MultiYAxesVisualization
                       title={summaryItem.title}
                       data={summaryItem.data}
                       onClickBar={this.navigateToNextViewType}
                       onClickBack={this.navigateToPrevViewType}
                       onClickNext={this.navigateToNextViewType}
                       rootClass="ShipmentsVisualization"
                       showNext={showNext}
                       showPrev={showPrev}
                     />
                     <LineDivider
                      orientation={HORIZONTAL_LINE}
                     />
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
                )
               });
              }
            }
          />
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
