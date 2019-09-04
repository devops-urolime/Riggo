import React from 'react';
import PropTypes from 'prop-types';
import './TotalSummary.scss';
import Typography from '@material-ui/core/Typography/Typography';

const TotalSummary = ({title, legend}) => {
  return(
    <div className="TotalSummary">
      <Typography className="TotalSummary__Legend" variant="h5" component="h2">
        {legend}
      </Typography>
      <Typography className="TotalSummary__Title" variant="body2" component="p">
        {title}
      </Typography>
    </div>
  );
};


TotalSummary.propTypes = {
  title: PropTypes.string,
  legend: PropTypes.string,
};


export default TotalSummary;
