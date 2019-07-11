import React from 'react';
import PropTypes from 'prop-types';
import './CardSummary.scss';

const CardSummary = ({number, label}) => {
  return (
    <div className="CardSummary">
       <p className="number">{number}</p>
       <p className="label">{label}</p>
    </div>
  );
};

CardSummary.propTypes = {
  number: PropTypes.number,
  label: PropTypes.string,
};

export default CardSummary;
