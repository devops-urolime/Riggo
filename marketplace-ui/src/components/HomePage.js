import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { withRouter } from 'react-router-dom';
import CardSummary from './CardSummary';
import './HomePage.scss';
import Grid from '@material-ui/core/Grid';
import TitleSection from './TitleSection';
import Paper from '@material-ui/core/Paper';
import { DARK2, NIVO, SAMPLE_DATA_PIE_1, SAMPLE_DATA_PIE_2 } from './PieVisualization';
import PieVisualization from './PieVisualization';
import BarVisualization, { SAMPLE_DATA_BAR } from './BarVisualization';

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
    const { isBarGroupMode } = this.state;
      return (
        <Grid
          container
          spacing={0}
          direction="column"
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
            container
            spacing={0}
            direction="row"
            justify="center"
            alignItems="center"
          >
            { pipeLineSummary &&
              pipeLineSummary.map((item, index) => {
              return (
                <Grid sm={3} xs={4} key={`card-summary-${index}`} item>
                  <CardSummary number={item.number} label={item.label}/>
                </Grid>
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
                  data={SAMPLE_DATA_BAR}
                  colorsScheme={DARK2}
                  rootClass="StatusVisualization"
                  groupMode={isBarGroupMode}
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
