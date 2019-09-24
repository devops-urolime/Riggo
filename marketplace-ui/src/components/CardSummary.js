import React from 'react';
import PropTypes from 'prop-types';
import './CardSummary.scss';
import Typography from '@material-ui/core/Typography';

const CardSummary = ({number, label}) => {
  return (
    <div className="CardSummary CardSummary__MuiPaper-root">
        <Typography className="CardSummary__number" variant="h5" component="h2">
          {number}
        </Typography>
        <Typography className="CardSummary__label" variant="body2" component="p">
          {label}
        </Typography>
    </div>
  );
};

CardSummary.propTypes = {
  number: PropTypes.number,
  label: PropTypes.string,
};

export default CardSummary;
