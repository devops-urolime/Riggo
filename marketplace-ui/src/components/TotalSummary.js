import React from 'react';
import PropTypes from 'prop-types';
import './TotalSummary.scss';
import Typography from '@material-ui/core/Typography/Typography';

const TotalSummary = ({title, legend}) => {
  return(
    <div className="TotalSummary">
      <Typography className="TotalSummary__Title" component="h2" variant="h6"  gutterBottom>
        {title}
      </Typography>
      <Typography component="p" variant="h4">
        {legend}
      </Typography>
    </div>
  );
};


TotalSummary.propTypes = {
  title: PropTypes.string,
  legend: PropTypes.string,
};


export default TotalSummary;
