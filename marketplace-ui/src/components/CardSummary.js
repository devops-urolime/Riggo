import React from 'react';
import PropTypes from 'prop-types';
import './CardSummary.scss';
import Card from '@material-ui/core/Card';
import CardContent from '@material-ui/core/CardContent';
import Typography from '@material-ui/core/Typography';

const CardSummary = ({number, label}) => {
  return (
    <Card className="CardSummary">
      <CardContent>
        <Typography className="CardSummary__number" variant="h5" component="h2">
          {number}
        </Typography>
        <Typography className="CardSummary__label" variant="body2" component="p">
          {label}
        </Typography>
      </CardContent>
    </Card>
  );
};

CardSummary.propTypes = {
  number: PropTypes.number,
  label: PropTypes.string,
};

export default CardSummary;
