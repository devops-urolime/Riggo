import React, {Component} from 'react';
import './Login.scss';
import { WebAuth } from "auth0-js";
import { TextField, Button } from '@material-ui/core';
import { AUTH_CONFIG, AUTH_CONFIG_REALM } from '../config';
import { LOGO_MAIN_LOGIN } from './Icon';
import Icon from './Icon';
import Grid from '@material-ui/core/Grid/Grid';
import Snackbar from '@material-ui/core/Snackbar';
import Fade from '@material-ui/core/Fade';

const webAuth = new WebAuth({
  domain: AUTH_CONFIG.domain,
  clientID: AUTH_CONFIG.clientId,
  redirectUri: AUTH_CONFIG.callbackUrl,
  audience: AUTH_CONFIG.audience,
  responseType: AUTH_CONFIG.responseType,
  scope: AUTH_CONFIG.scope
});

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
    this.handleAuthentication(this.state.email, this.state.password);
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

  handleAuthentication = (email, password) => {
    if(email && password){
      webAuth.login({
        realm: AUTH_CONFIG_REALM,
        username: email,
        password: password,
      }, (err) => this.loginErrResult(err));
    } else {
      this.showMessage("Please complete email and password for login.");
    }

  };

  forgotPassword = (email) => {
    if(email){
      webAuth.changePassword({
        connection: AUTH_CONFIG_REALM,
        email
      }, (err) => {
        if(err){
          this.loginErrResult(err);
        }else {
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
      <div className="MainLoginOverlay">
        <Grid container spacing={0} {...gridConfig}>
          <form id="universal-login-form" onSubmit={(evt)=> this.loginFormAction(evt)}>
            <Grid item xs={gridWidth} >
              <Icon name={LOGO_MAIN_LOGIN}/>
            </Grid>
            <Grid item xs={gridWidth}>
              <p className="MainTitle">
                Sign In to Shipper
              </p>
              <p className="SubTitle">
                Enter your details below
              </p>
            </Grid>
            <Grid item xs={gridWidth}>
              <div className="InputGroup">
                <TextField
                  id="outlined-email-input"
                  label="Email"
                  type="email"
                  name="email"
                  autoComplete="email"
                  margin="normal"
                  variant="outlined"
                  onChange={this.onEmailChange}
                  className="TextField"
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
                  autoComplete="current-password"
                  margin="normal"
                  variant="outlined"
                  onChange={this.onPasswordChange}
                  required
                />
              </div>
            </Grid>
            <Grid item xs={gridWidth}>
              <div onClick={() => this.forgotPassword(email)}  className="ForgotPassword" role="link" id="forgot-password">
                Forgot Password ?
              </div>
            </Grid>
            <Grid item xs={gridWidth}>
              <div className="ButtonGroup">
                <Button className="Login-btn" type="submit" variant="contained" color="primary">
                  Sign In
                </Button>
              </div>
            </Grid>
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

export default Login;
