import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { withRouter } from 'react-router-dom';
import CardSummary from './CardSummary';
import './HomePage.scss';
import Grid from '@material-ui/core/Grid';
import TitleSection from './TitleSection';
import Paper from '@material-ui/core/Paper';
import PieVisualization, {
  DARK2,
  NIVO,
  SAMPLE_DATA_PIE_1,
  SAMPLE_DATA_PIE_2
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
    const card = {
      number: item.count,
      label: item.name
    };
    return card;
  });
};

class HomePage extends Component {

  constructor(props) {
    super(props);
    this.state = {
      isBarGroupMode: false,
    };
  }

  componentDidMount() {
    this.props.loadPipeLineSummary();
  }

  toggleBarGroup = ()=> {
    this.setState(prevState => ({
      isBarGroupMode: !prevState.isBarGroupMode
    }));
  };

  render(){
    const { pipeLineSummary } = this.props;
    const pipeLineSummaryBar = digestDataToBarVisualization(pipeLineSummary);
    const pipeLineSummaryCard = digestDataToCardVisualization(pipeLineSummary);
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
          { pipeLineSummaryCard &&
            pipeLineSummaryCard.map((item, index) => {
            return (
              <Grid xs={4} key={`card-summary-${index}`} item>
                <CardSummary number={item.number} label={item.label}/>
              </Grid>
            );
          })}
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
                  data={SAMPLE_DATA_PIE_1}
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
                  data={SAMPLE_DATA_PIE_2}
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
  loadPipeLineSummary: PropTypes.func,
};

HomePage.defaultProps = {
  pipeLineSummary: []
};
export default withRouter(HomePage);
