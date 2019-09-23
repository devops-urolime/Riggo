import React from 'react';
import PropTypes from 'prop-types';
import Grid from '@material-ui/core/Grid/Grid';
import Paper from '@material-ui/core/Paper/Paper';
import './Section.scss';
import Divider from '@material-ui/core/Divider/Divider';


const Section = ({ top, content }) =>{
  return(
    <Grid
      container
      spacing={0}
      direction="row"
      alignItems="center"
      className="Section"
    >
      <Grid item xs={12}>
        <Paper className="Section__MuiPaper-root">
          <Grid
            container
            spacing={0}
            direction="row"
            justify="flex-start"
            alignItems="flex-start"
          >
            <Grid item xs={12}>
              {top()}
              <Divider />
            </Grid>
          </Grid>
          <Grid
            container
            direction="row"
            justify="center"
            alignItems="center"
          >
            {content()}
          </Grid>
        </Paper>
      </Grid>
    </Grid>
  );
};


Section.propTypes = {
  top: PropTypes.func,
  content: PropTypes.func,
};


export default Section;
