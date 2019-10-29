import React from 'react';
import {
  ComposedChart, Line, Bar, XAxis, YAxis, Legend, ResponsiveContainer
} from 'recharts';
import './MultiYAxesVisualization.scss';
import PropTypes from 'prop-types';
import Typography from '@material-ui/core/Typography/Typography';
import Icon from '@material-ui/core/Icon';
import Grid from '@material-ui/core/Grid/Grid';

const MultiYAxesVisualization = ({
   onClickBar,
   data,
   rootClass,
   title,
   onClickBack,
   onClickNext,
   showNext,
   showPrev,
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
        <Grid xs={12} item>
          <Typography className="TitleVisualization" component="h2" variant="h6"  gutterBottom>
            {
              showPrev &&
              <Icon className="NavigateBack" onClick={onClickBack}>arrow_back_ios</Icon>
            }
            {title}
            {
              showNext &&
              <Icon className="NavigateNext" onClick={onClickNext}>arrow_back_ios</Icon>
            }
          </Typography>
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
