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
    "In Transit": 6,
    "In TransitColor": "hsl(9, 87%, 67%)",
    "At Delivery": 7,
    "At DeliveryColor": "hsl(9, 87%, 67%)"
  },
  {
    "status": "Delivered",
    "Pending Docs": 9,
    "Pending DocsColor": "hsl(9, 87%, 67%)",
    "Docs Received": 8,
    "Docs ReceivedColor": "hsl(9, 87%, 67%)",
    "Invoiced": 11,
    "InvoicedColor": "hsl(9, 87%, 67%)"
  }
];

export const BAR_DARK2 = "dark2";

const BarVisualization = ({ data, rootClass, colorsScheme, groupMode}) => {
  const customLabelFormat = (d)=>{
    return (!groupMode) ? `${d.id} ${d.value}` : `${d.value}`
  };
  let configBar = {
    data: data,
    enableGridY: false,
    enableGridX: false,
    keys:[
          'Quoted',
          'Booked',
          'At Pickup',
          'In Transit',
          'At Delivery',
          'Pending Docs',
          'Docs Received',
          'Invoiced'
         ],
    indexBy: "status",
    margin:{ top: 20, right: 30, bottom: 50, left: 15 },
    padding:0,
    colors:{ scheme: colorsScheme },
    axisTop:null,
    axisRight:null,
    axisBottom:null,
    axisLeft:null,
    labelSkipWidth:12 ,
    labelSkipHeight:12,
    borderColor:{ from: 'color', modifiers: [ [ 'darker', 1.6 ] ] },
    labelTextColor:{ from: 'color', modifiers: [ [ 'darker', 1.6 ] ] },
    label: customLabelFormat,
    animate:true,
    motionStiffness:90,
    motionDamping:15,
    isInteractive:false,
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
