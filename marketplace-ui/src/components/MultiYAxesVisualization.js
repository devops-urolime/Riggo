import React from 'react';
import {
  ComposedChart, Line, Bar, XAxis, YAxis, Legend, ResponsiveContainer
} from 'recharts';
import './MultiYAxesVisualization.scss';
import PropTypes from 'prop-types';
import Typography from '@material-ui/core/Typography/Typography';
import Icon from '@material-ui/core/Icon';
import Grid from '@material-ui/core/Grid/Grid';
import Grow from '@material-ui/core/Grow';

const getTimingEffect = (index) => {
  const GROW_TIMING_EFFECT = [ 600, 800, 1000 ];
  return ( index < GROW_TIMING_EFFECT.length ) ? GROW_TIMING_EFFECT[index] : 500;
};

const getConfigGrow = (index) => {
  const configGrow = {
    in:true
  };
  if (index > 0){
    configGrow['timeout'] = getTimingEffect(index);
    configGrow['style'] = { transformOrigin: '0 0 0' };
  }
  return configGrow;
};

const configGrow =  getConfigGrow(1) ;
const MultiYAxesVisualization = ({
   onClickBar,
   data,
   rootClass,
   title,
   onClickBack,
   onClickNext,
   showNext,
   showPrev
}) => {
  return (
    <div className={"MultiYAxesVisualization "+rootClass}>
      <Grid
        spacing={0}
        container
        direction="row"
        justify="center"
        alignItems="center"
        className="NavigateSection"
      >
        <Grid xs={4} item>
          {
            showPrev &&
            <Grow {...configGrow}>
              <Icon className="NavigateBack" onClick={onClickBack}>arrow_back_ios</Icon>
            </Grow>
          }
        </Grid>
        <Grid xs={4} item>
          <Typography className="TitleVisualization">
              {title}
          </Typography>
        </Grid>
        <Grid xs={4} item>
          {
            showNext &&
            <Grow {...configGrow}>
              <Icon className="NavigateNext" onClick={onClickNext}>arrow_back_ios</Icon>
            </Grow>
          }
        </Grid>
      </Grid>
      <ResponsiveContainer>
          <ComposedChart
            width={324}
            height={370}
            data={data}
            margin={{
              top: 20, right: 0, bottom: 55, left: 0,
            }}
          >
            <XAxis dataKey="date" />
            <YAxis yAxisId="left" />
            <YAxis yAxisId="right" orientation="right" />
            <Legend
              iconType="square"
              align="left"
            />
            <Bar
              yAxisId="left"
              dataKey="Shipment Delivered"
              barSize={60}
              onClick={onClickBar}
            />
            <Line
              yAxisId="right"
              type="monotone"
              dataKey="Cost Per Mile"
            />
          </ComposedChart>
      </ResponsiveContainer>
    </div>
  )
};


MultiYAxesVisualization.propTypes = {
  onClickBar: PropTypes.func,
  onClickBack: PropTypes.func,
  onClickNext: PropTypes.func,
  title: PropTypes.string,
  rootClass: PropTypes.string,
  data: PropTypes.array,
  showNext: PropTypes.bool,
  showPrev: PropTypes.bool,
};

export default MultiYAxesVisualization;
