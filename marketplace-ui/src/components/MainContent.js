import React from 'react';
import PropTypes from 'prop-types';

const MainContent = ({children}) => {
  return (
    <div>
      {children}
    </div>
  );
};

MainContent.propTypes = {
  children: PropTypes.oneOfType([
    PropTypes.object,
    PropTypes.bool
  ]),
};

export default MainContent;
