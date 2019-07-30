import React from 'react';
import PropTypes from 'prop-types';
import { ResponsivePie } from '@nivo/pie';
import './PieVisualization.scss';
export const SAMPLE_DATA_PIE_1 = [
  {
    "id": "Early",
    "label": "Early",
    "value": 6.8,
    "color": "hsl(131, 70%, 50%)"
  },
  {
    "id": "On time",
    "label": "On time",
    "value": 36.9,
    "color": "hsl(118, 18%, 66%)"
  },
  {
    "id": "Delayed",
    "label": "Delayed",
    "value": 56.3,
    "color": "hsl(310, 70%, 50%)"
  }
];

export const SAMPLE_DATA_PIE_2 = [
  {
    "id": "Early",
    "label": "Early",
    "value": 6.8,
    "color": "hsl(131, 70%, 50%)"
  },
  {
    "id": "On time",
    "label": "On time",
    "value": 36.9,
    "color": "hsl(118, 18%, 66%)"
  },
  {
    "id": "Delayed",
    "label": "Delayed",
    "value": 56.3,
    "color": "hsl(310, 70%, 50%)"
  }
];

export const NIVO = "nivo";
export const DARK2 = "dark2";

const PieVisualization = ({ data, rootClass, colorsScheme}) => {
  const customLabelFormat = (d)=> {
    return (<p>{d.id} {d.percent}% </p>)
  };
  return (
    <div className={"PieVisualization " +rootClass}>
      <ResponsivePie
              tooltip= {customLabelFormat}
              colors={{ scheme: colorsScheme }}
              data={data}
              margin={{ top: 40, right: 80, bottom: 80, left: 80 }}
              sortByValue={true}
              innerRadius={0.65}
              padAngle={0.7}
              enableRadialLabels={false}
              sliceLabel={(item) => `${item.value}`}
              slicesLabelsSkipAngle={10}
              slicesLabelsTextColor="#333333"
              animate={true}
              motionStiffness={90}
              motionDamping={15}
              legends={[
                  {
                      anchor: 'bottom',
                      direction: 'row',
                      translateY: 56,
                      itemWidth: 100,
                      itemHeight: 18,
                      itemTextColor: '#999',
                      symbolSize: 18,
                      symbolShape: 'circle',
                      effects: [
                          {
                              on: 'hover',
                              style: {
                                  itemTextColor: '#000'
                              }
                          }
                      ]
                  }
              ]}
          />
  </div>
  );
};

PieVisualization.propTypes = {
  data: PropTypes.array,
  rootClass: PropTypes.string,
  colorsScheme: PropTypes.string,
};

export default PieVisualization;
