import React from 'react';
import PropTypes from 'prop-types';
import { ResponsiveBar } from '@nivo/bar';
import './BarVisualization.scss';
export const SAMPLE_DATA_BAR = [
  {
    "status": "Pending",
    "Quoted": 32,
    "QuotedColor": "hsl(9, 87%, 67%)",
    "Booked": 26,
    "BookedColor": "hsl(9, 87%, 67%)"
  },
  {
    "status": "In Transit",
    "Dispatched": 9,
    "DispatchedColor": "hsl(9, 87%, 67%)",
    "At Pickup": 8,
    "At PickupColor": "hsl(9, 87%, 67%)",
    "In Transit Pickup": 6,
    "In Transit PickupColor": "hsl(9, 87%, 67%)",
    "At Delivery Pickup": 7,
    "At Delivery PickupColor": "hsl(9, 87%, 67%)"
  },
  {
    "status": "Delivered",
    "Pending Documents": 9,
    "Pending DocumentsColor": "hsl(9, 87%, 67%)",
    "Documents Received": 8,
    "Documents ReceivedColor": "hsl(9, 87%, 67%)",
    "Invoiced": 11,
    "InvoicedColor": "hsl(9, 87%, 67%)"
  }
];

export const NIVO = "nivo";
export const DARK2 = "dark2";

const BarVisualization = ({ data, rootClass, colorsScheme, groupMode}) => {
  let configBar = {
    data: data,
    enableGridY: false,
    enableGridX: false,
    keys:[
          'Quoted',
          'Booked',
          'At Pickup',
          'In Transit Pickup',
          'At Delivery Pickup',
          'Pending Documents',
          'Documents Received',
          'Invoiced'
         ],
    indexBy: "status",
    margin:{ top: 20, right: 30, bottom: 50, left: 5 },
    padding:0.3,
    colors:{ scheme: colorsScheme },
    defs:[
            {
                id: 'dots',
                type: 'patternDots',
                background: 'inherit',
                color: '#38bcb2',
                size: 4,
                padding: 1,
                stagger: true
            },
            {
                id: 'lines',
                type: 'patternLines',
                background: 'inherit',
                color: '#eed312',
                rotation: -45,
                lineWidth: 6,
                spacing: 10
            }
        ],
    axisTop:null,
    axisRight:null,
    axisBottom:null,
    axisLeft:null,
    labelSkipWidth:12 ,
    labelSkipHeight:12,
    borderColor:{ from: 'color', modifiers: [ [ 'darker', 1.6 ] ] },
    labelTextColor:{ from: 'color', modifiers: [ [ 'darker', 1.6 ] ] },
    animate:true,
    motionStiffness:90,
    motionDamping:15,
  };
  if(groupMode){
    configBar.groupMode = "grouped";
  }
  return (
    <div className={"BarVisualization " +rootClass}>
      <ResponsiveBar
          {...configBar}
      />
  </div>
  );
};

BarVisualization.propTypes = {
  data: PropTypes.array,
  rootClass: PropTypes.string,
  colorsScheme: PropTypes.string,
  groupMode: PropTypes.bool,
};

export default BarVisualization;
