import React, { PureComponent } from 'react';
import {
  ComposedChart, Line, Bar, XAxis, YAxis, Legend,
} from 'recharts';

import './MultiYAxesVisualization.scss';

const data = [
  {
    date: 'July 1', "Shipment Delivered": 55, "Cost Per Mile": 5.36,
  },
  {
    date: 'July 2', "Shipment Delivered": 40, "Cost Per Mile": 4.10 ,
  },
  {
    date: 'July 3', "Shipment Delivered": 35, "Cost Per Mile":  3.20,
  },
  {
    date: 'July 4', "Shipment Delivered": 65, "Cost Per Mile": 8.57 ,
  },
  {
    date: 'July 5', "Shipment Delivered": 15, "Cost Per Mile":  1.7,
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
        <Bar
          yAxisId="left"
          dataKey="Shipment Delivered"
          barSize={20}
          fill="#FECEA8"
        />
        <Line
          yAxisId="right"
          type="monotone"
          dataKey="Cost Per Mile"
          stroke="#E84B5E"
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
