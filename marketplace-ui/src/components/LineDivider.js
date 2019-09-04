import React from 'react';
import PropTypes from 'prop-types';
import './LineDivider.scss';

export const HORIZONTAL_LINE = "--horizontal";
export const VERTICAL_LINE = "--vertical";

const LineDivider = ({orientation}) => {
   return (
     <div className={`LineDivider LineDivider${orientation}`}/>
   )
};

LineDivider.propTypes = {
  orientation: PropTypes.string,
};


export default LineDivider;
