import React from 'react';
import PropTypes from 'prop-types';
import { withRouter } from 'react-router-dom';
import Button from '@material-ui/core/Button';
import './CardSummary.scss';
import CardSummary from './CardSummary';
import Grid from '@material-ui/core/Grid';

const HomePage = (props) => {
  const {auth:{login, logout}} = props;
  return (
    <Grid container spacing={3}>
      <Grid item xs={12}>
        <Grid container justify="center" spacing={3}>
          <Grid item>
            <CardSummary number={24} label="Pending"/>
          </Grid>
          <Grid item>
            <CardSummary number={45} label="In Transit"/>
          </Grid>
          <Grid item>
            <CardSummary number={78} label="Delivered"/>
          </Grid>
        </Grid>
      </Grid>
      <Grid item xs={12}>
        <Grid container justify="center" spacing={2}>
          <Grid item>
            <Button variant="contained" color="primary" onClick={() => login()}>
              Login
            </Button>
          </Grid>
          <Grid item>
            <Button variant="contained" color="primary" onClick={() => logout() }>
              Logout
            </Button>
          </Grid>
        </Grid>
      </Grid>
    </Grid>
  );
};

HomePage.propTypes = {
  auth: PropTypes.object,
};
export default withRouter(HomePage);
