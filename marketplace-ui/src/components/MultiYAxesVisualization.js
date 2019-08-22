import React from 'react';
import {
  ComposedChart, Line, Bar, XAxis, YAxis, Legend, ResponsiveContainer
} from 'recharts';

import './MultiYAxesVisualization.scss';
import PropTypes from 'prop-types';
import Typography from '@material-ui/core/Typography/Typography';
import Icon from '@material-ui/core/Icon';

const MultiYAxesVisualization = ({onClickBar, data, rootClass, title}) => {
  return (
    <div className={"MultiYAxesVisualization "+rootClass}>
      <Typography className="TitleVisualization">
        {title}
      </Typography>
      <ResponsiveContainer>
        <ComposedChart
          width={324}
          height={370}
          data={data}
          margin={{
            top: 20, right: 0, bottom: 35, left: 0,
          }}
        >
          <XAxis dataKey="date" />
          <YAxis yAxisId="left" />
          <YAxis yAxisId="right" orientation="right" />
          <Legend
            iconType="square"
            align="center"
          />
          <Bar
            yAxisId="left"
            dataKey="Shipment Delivered"
            barSize={20}
            fill="#FECEA8"
            onClick={onClickBar}
          />
          <Line
            yAxisId="right"
            type="monotone"
            dataKey="Cost Per Mile"
            stroke="#E84B5E"
          />
        </ComposedChart>
      </ResponsiveContainer>
    </div>
  )
};


MultiYAxesVisualization.propTypes = {
  onClickBar: PropTypes.func,
  onClickBack: PropTypes.func,
  title: PropTypes.string,
  rootClass: PropTypes.string,
  data: PropTypes.array,
};

export default MultiYAxesVisualization;
