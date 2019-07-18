import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { withRouter } from 'react-router-dom';
import Button from '@material-ui/core/Button';
import './CardSummary.scss';
import CardSummary from './CardSummary';
import Grid from '@material-ui/core/Grid';

class HomePage extends Component {
  componentDidMount() {
    this.props.loadPipeLineSummary();
  }
  render(){
    const {pipeLineSummary} = this.props;
      return (
        <Grid container spacing={3}>
          <Grid item xs={12}>
            <Grid container justify="center" spacing={3}>
              { pipeLineSummary && pipeLineSummary.map((item, index) => {
                return (
                  <Grid key={`card-summary-${index}`} item>
                    <CardSummary number={item.number} label={item.label}/>
                  </Grid>
                );
              })}
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
