import React, { PureComponent } from 'react';
import {
  ComposedChart, Line, Bar, XAxis, YAxis, Legend,
} from 'recharts';

import './MultiYAxesVisualization.scss';

const data = [
  {
    date: 'July 1', "Shipment Delivered": 150, "Cost Per Mile": 5.36,
  },
  {
    date: 'July 2', "Shipment Delivered": 100, "Cost Per Mile": 4.10 ,
  },
  {
    date: 'July 3', "Shipment Delivered": 75, "Cost Per Mile":  3.20,
  },
  {
    date: 'July 4', "Shipment Delivered": 200, "Cost Per Mile": 8.57 ,
  },
  {
    date: 'July 5', "Shipment Delivered": 125, "Cost Per Mile":  1.7,
  },
  {
    date: 'July 6', "Shipment Delivered": 128, "Cost Per Mile": 3.34 ,
  },
];

class MultiYAxesLineAndBar extends PureComponent {
  render() {
    return (
      <ComposedChart
        width={324}
        height={360}
        data={data}
        margin={{
          top: 30, right: 0, bottom: 20, left: 0,
        }}
      >
        <XAxis dataKey="date" />
        <YAxis yAxisId="left" />
        <YAxis yAxisId="right" orientation="right" />
        <Legend
          iconType="square"
          align="center"
        />
        <Line
          yAxisId="left"
          type="monotone"
          dataKey="Shipment Delivered"
          stroke="#E84B5E"
        />
        <Bar
          yAxisId="right"
          dataKey="Cost Per Mile"
          barSize={20}
          fill="#FECEA8"
        />
      </ComposedChart>
    );
  }
}

const MultiYAxesVisualization = () => {
  return (
    <div className="MultiYAxesVisualization">
       <MultiYAxesLineAndBar />
    </div>
  )
};
export default MultiYAxesVisualization;
