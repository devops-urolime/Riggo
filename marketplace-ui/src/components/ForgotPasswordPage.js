import React, { Component } from 'react';
import './ForgotPasswordPage.scss';
import {
  Button,
  TextField
} from '@material-ui/core';
import { AUTH_CONFIG_REALM } from '../config';
import Icon, { LOGO_MAIN_LOGIN } from './Icon';
import Grid from '@material-ui/core/Grid/Grid';
import Paper from '@material-ui/core/Paper';
import { webAuth } from '../lib/auth';

const gridWidth = 12;
const gridConfig = {
  direction:"row",
  justify:"center",
  alignItems:"center",
};

class ForgotPasswordPage extends Component{

  constructor(props) {
    super(props);
    this.state = {
      email: "",
      password:"",
      isOpenMessage: false,
      message: "",
      isSingUp: false,
    };
  }

  onEmailChange = (evt) =>{
     this.setState({email: evt.target.value});
  };

  loginFormAction = (evt) => {
    evt.preventDefault();
    const { email } = this.state;
    this.forgotPassword(email);
  };

  showMessage = (message) => {
    this.setState({
      isOpenMessage: true,
      message
    });
  };

  loginErrResult = (err) => {
      if(err){
        if(err.description){
          this.showMessage(err.description);
        } else {
          this.showMessage("Something went wrong, try again later");
        }
      }
  };

  forgotPassword = (email) => {
    if(email){
      webAuth.changePassword({
        connection: AUTH_CONFIG_REALM,
        email
      }, (err) => {
        if (err) {
          this.loginErrResult(err);
        } else {
          this.showMessage("We've just sent you an email to reset your password.");
        }
      });
    } else {
      this.showMessage("Please complete email to reset your password.");
    }
  };

  render(){
    const { isOpenMessage, message, email }= this.state;
    return(
      <div className="ForgotPasswordPage">
          <Grid container spacing={0} {...gridConfig}>
            <form id="universal-login-form" onSubmit={(evt)=> this.loginFormAction(evt)}>
              <Grid container spacing={0} {...gridConfig}>
                <Grid item xs={gridWidth} >
                  <Icon name={LOGO_MAIN_LOGIN}/>
                </Grid>
              </Grid>
              <Paper className="SecondaryLoginOverlay">
                <Grid container spacing={0} {...gridConfig}>
                  <Grid item xs={gridWidth}>
                    <div className="InputGroup">
                      <TextField
                        id="outlined-email-input"
                        label="Email"
                        type="email"
                        name="email"
                        autoComplete="new-email"
                        margin="normal"
                        variant="outlined"
                        onChange={this.onEmailChange}
                        fullWidth
                        required
                      />
                    </div>
                  </Grid>
                  <Grid item xs={gridWidth}>
                    <div className="ButtonGroup">
                      <Button
                        fullWidth
                        className="Login-btn"
                        type="submit"
                        variant="contained"
                        color="primary"
                        onClick={() => this.forgotPassword(email)}
                      >
                        Send Email
                      </Button>
                    </div>
                  </Grid>
                  <Grid item xs={gridWidth}>
                    {
                      isOpenMessage &&
                      <div className="SuccessMessage">
                        <span id="message-id">{message}</span>
                      </div>
                    }
                  </Grid>
                </Grid>
              </Paper>
            </form>
          </Grid>
      </div>
    );
  }
}

export default ForgotPasswordPage;
