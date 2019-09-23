import React from 'react';
import PropTypes from 'prop-types';
import './StackVisualization.scss';
import Typography from '@material-ui/core/Typography';
import Grid from '@material-ui/core/Grid';
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

const SubStatuses = ({items}) => {
  return items.map((statusItem, idx2) =>{
     return(
       <div className={`SubStatusBox SubStatus${5-(idx2+1)}`} key={`sub-stack-box-${idx2}`}>
         <Typography className="CardSummary__label" variant="body2" component="p">
           {statusItem.name}, {statusItem.count}
         </Typography>
       </div>
     )
   });
};
const StackVisualization = ({data, rootClass}) => {
   console.log(data);
   return (
       <Grid
         container
         spacing={4}
         direction="row"
         alignItems="baseline"
         className={"StackVisualization " + rootClass}
       >
       {data && data.map((item, idx) => {
         const configGrow =  getConfigGrow(idx) ;
         const {name, subStatuses} = item;
         return(
           <Grid xs={4} key={`stack-box-${idx}`} item>
             <Grow {...configGrow}  key={`stack-box-grow-${idx}`}>
              <>
                <Typography className="TitleStack" variant="h5" component="h5">
                  {name}
                </Typography>
                {
                subStatuses &&
                <SubStatuses items={subStatuses}/>
                }
              </>
             </Grow>
           </Grid>
         )
       })}
     </Grid>
   );
};

StackVisualization.propTypes = {
  data: PropTypes.array,
  rootClass: PropTypes.string,
};

export default StackVisualization;
