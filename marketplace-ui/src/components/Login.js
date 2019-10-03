import React, { Component } from 'react';
import './Login.scss';
import { Button, TextField } from '@material-ui/core';
import { APP_PATH_FORGOT_PASSWORD, AUTH_CONFIG_REALM } from '../config';
import Icon, { LOGO_MAIN_LOGIN } from './Icon';
import Grid from '@material-ui/core/Grid/Grid';
import Snackbar from '@material-ui/core/Snackbar';
import Fade from '@material-ui/core/Fade';
import Paper from '@material-ui/core/Paper';
import { webAuth } from '../lib/auth';
import { withRouter } from 'react-router-dom';
import PropTypes from 'prop-types';

const gridWidth = 12;
const gridConfig = {
  direction:"row",
  justify:"center",
  alignItems:"center",
};

class Login extends Component{

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

  onPasswordChange = (evt) =>{
     this.setState({password: evt.target.value});
  };

  loginFormAction = (evt) => {
    evt.preventDefault();
    const { email, password, isSingUp} = this.state;
    this.handleAuthentication(email, password, isSingUp);
  };

  showMessage = (message) => {
    this.setState({
      isOpenMessage: true,
      message
    });
  };

  closeMessage = () => {
    this.setState({
      isOpenMessage: false,
      message:""
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

  handleAuthentication = (email, password, isSingUp) => {
    if(isSingUp){
       this.signUp(email,password);
    } else {
      if(email && password){
        webAuth.login({
          realm: AUTH_CONFIG_REALM,
          username: email,
          password: password,
        }, (err) => this.loginErrResult(err));
      } else {
        this.showMessage("Please complete email and password for login.");
      }
    }
  };

  signUp = (email, password) => {
    if(email && password){
      webAuth.signup({
        connection: AUTH_CONFIG_REALM,
        email,
        password
      }, (err) => {
        if (err) {
          this.loginErrResult(err);
        } else {
          this.showMessage("We've just sent you an email to verify your account.");
        }
      });
    } else {
      this.showMessage("Please complete email and password for Sing Up.");
    }
  };

  render(){
    const { isOpenMessage, message }= this.state;
    const { history }= this.props;
    return(
      <div className="MainLoginOverlay">
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
                    <div className="InputGroup InputGroup--last">
                      <TextField
                        id="outlined-password-input"
                        label="Password"
                        type="password"
                        autoComplete="new-password"
                        margin="normal"
                        variant="outlined"
                        onChange={this.onPasswordChange}
                        fullWidth
                        required
                      />
                    </div>
                  </Grid>
                  <Grid item xs={gridWidth}>
                    <div onClick={() => history.push(APP_PATH_FORGOT_PASSWORD)}  className="ForgotPassword" role="link" id="forgot-password">
                      Forgot Password ?
                    </div>
                  </Grid>
                  <Grid item xs={gridWidth}>
                    <div className="ButtonGroup">
                      <Button fullWidth className="Login-btn" type="submit" variant="contained" color="primary">
                        Log In
                      </Button>
                    </div>
                  </Grid>
                  <Grid item xs={gridWidth}>
                    <div className="ButtonGroup">
                      <p className="SignUp" role="link"  >

                      </p>
                    </div>
                  </Grid>
                </Grid>
              </Paper>
              <Snackbar
                 open={isOpenMessage}
                 TransitionComponent={Fade}
                 onClose={this.closeMessage}
                 ContentProps={{
                   'aria-describedby': 'message-id',
                 }}
                 message={<span id="message-id">{message}</span>}
              />
            </form>
          </Grid>

      </div>
    );
  }
}

Login.propTypes = {
  history: PropTypes.object,
};

export default withRouter(Login);
