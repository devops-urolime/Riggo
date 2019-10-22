import React from 'react';
import PropTypes from 'prop-types';
import './TotalSummary.scss';
import Typography from '@material-ui/core/Typography/Typography';

const TotalSummary = ({title, legend, center}) => {
  return(
    <div className={`TotalSummary${(center)? "--center": ""}`}>
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
  center: PropTypes.bool,
};


export default TotalSummary;
