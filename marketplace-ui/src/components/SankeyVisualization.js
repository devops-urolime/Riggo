import React from 'react';
import PropTypes from 'prop-types';
import { ResponsiveSankey } from '@nivo/sankey';
import './SankeyVisualization.scss';

export const SAMPLE_DATA_SANDKEY = {
  "nodes": [
    {
      "id": "Pending",
      "color": "hsl(336, 70%, 50%)",
      "value": 58
    },
    {
      "id": "In Transit",
      "color": "hsl(154, 70%, 50%)",
      "value": 30
    },
    {
      "id": "Delivered",
      "color": "hsl(189, 70%, 50%)",
      "value": 28
    }
  ],
  "links": [
    {
      "source": "Pending",
      "target": "In Transit",
      "value": 28
    },
    {
      "source": "In Transit",
      "target": "Delivered",
      "value": 2
    }
  ]
};

const SankeyVisualization = ({ data, rootClass}) => {
  return (
    <div className={"SankeyVisualization " +rootClass}>
      <ResponsiveSankey
        data={data}
        margin={{ top: 40, right: 40, bottom: 40, left: 40 }}
        align="justify"
        colors={{ scheme: 'category10' }}
        nodeOpacity={1}
        nodeThickness={12}
        nodeInnerPadding={3}
        nodeSpacing={24}
        nodeBorderWidth={0}
        nodeBorderColor={{ from: 'color', modifiers: [ [ 'darker', 0.8 ] ] }}
        linkOpacity={0.5}
        linkHoverOthersOpacity={0.1}
        enableLinkGradient={true}
        labelPosition="outside"
        labelOrientation="vertical"
        labelPadding={16}
        labelTextColor="#fff"
        animate={true}
        motionStiffness={140}
        motionDamping={13}

    />
  </div>
  );
};

SankeyVisualization.propTypes = {
  data: PropTypes.object,
  rootClass: PropTypes.string,
};

export default SankeyVisualization;
