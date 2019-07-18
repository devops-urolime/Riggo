import React from 'react';
import PropTypes from 'prop-types';
import Typography from '@material-ui/core/Typography/Typography';
import './TitleSection.css';

const TitleSection = ({label}) => {
    return (
      <div className="TitleSection">
        <Typography variant="h6" gutterBottom>
          {label}
        </Typography>
      </div>
    );
};

TitleSection.propTypes = {
  label: PropTypes.string,
};

export default TitleSection;

