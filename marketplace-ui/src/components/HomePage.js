import React from 'react';
import PropTypes from 'prop-types';
import { withRouter } from 'react-router-dom';
import Button from '@material-ui/core/Button';
import './CardSummary.scss';
import CardSummary from './CardSummary';

const HomePage = (props) => {
  const {auth:{login, logout}} = props;
  return (
    <section>
      <section>
        <CardSummary number={24} label="Pending"/>
        <CardSummary number={45} label="In Transit"/>
        <CardSummary number={78} label="Delivered"/>
      </section>
      <Button variant="contained" color="primary" onClick={() => login()}>
        Login
      </Button>
      <Button variant="contained" color="primary" onClick={() => logout() }>
        Logout
      </Button>
    </section>
  );
};

HomePage.propTypes = {
  auth: PropTypes.object,
};
export default withRouter(HomePage);
