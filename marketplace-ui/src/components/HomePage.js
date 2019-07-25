import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { withRouter } from 'react-router-dom';
import CardSummary from './CardSummary';
import './HomePage.scss';
import Grid from '@material-ui/core/Grid';
import TitleSection from './TitleSection';
import SankeyVisualization, { SAMPLE_DATA } from './SankeyVisualization';
import Paper from '@material-ui/core/Paper';

class HomePage extends Component {
  componentDidMount() {
    this.props.loadPipeLineSummary();
  }
  render(){
    const {pipeLineSummary} = this.props;
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
            className="HomePage"
          >
            <Grid item xs={11}>
              <Paper className="HomePage__MuiPaper-root">
                <SankeyVisualization
                  data={SAMPLE_DATA}
                  rootClass="StatusVisualization"
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
