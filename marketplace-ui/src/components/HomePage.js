import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { withRouter } from 'react-router-dom';
import CardSummary from './CardSummary';
import './HomePage.scss';
import Grid from '@material-ui/core/Grid';
import TitleSection from './TitleSection';
import Paper from '@material-ui/core/Paper';
import Grow from '@material-ui/core/Grow';
import PieVisualization, {
  DARK2,
  NIVO
} from './PieVisualization';
import BarVisualization, { BAR_DARK2 } from './BarVisualization';

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

class HomePage extends Component {

  constructor(props) {
    super(props);
    this.state = {
      isBarGroupMode: true,
    };
  }

  componentDidMount() {
    this.props.loadPipeLineSummary();
    this.props.loadStopSummary();
  }

  toggleBarGroup = ()=> {
    this.setState(prevState => ({
      isBarGroupMode: !prevState.isBarGroupMode
    }));
  };

  render(){
    const { pipeLineSummary, stopSummary } = this.props;
    const pipeLineSummaryBar = digestDataToBarVisualization(pipeLineSummary);
    const pipeLineSummaryCard = digestDataToCardVisualization(pipeLineSummary);
    const stopSummaryPickUpPie = digestDataToPieVisualization(stopSummary, PICKUP_ROOT_PROP);
    const stopSummaryDeliveryPie = digestDataToPieVisualization(stopSummary, DELIVERY_ROOT_PROP);
    const { isBarGroupMode } = this.state;
      return (
        <Grid
          container
          spacing={0}
          direction="row"
          alignItems="center"
          className="HomePage"
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
            return (
              <Grow in={true} timeout={500}>
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
              <Paper className="HomePage__MuiPaper-root" onClick={() => this.toggleBarGroup()}>
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
              <TitleSection label="On Time Performance - Pickup"/>
            </Grid>
          </Grid>
          <Grid
            container
            spacing={0}
            direction="column"
            alignItems="center"
            className="HomePage"
          >
            <Grid item xs={11}>
              <Paper className="HomePage__MuiPaper-root">
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
            className="HomePage"
          >
            <Grid item xs={11}>
              <Paper className="HomePage__MuiPaper-root">
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

HomePage.propTypes = {
  pipeLineSummary: PropTypes.array,
  stopSummary: PropTypes.array,
  loadPipeLineSummary: PropTypes.func,
  loadStopSummary: PropTypes.func,
};

HomePage.defaultProps = {
  pipeLineSummary: [],
  stopSummary: [],
};
export default withRouter(HomePage);
