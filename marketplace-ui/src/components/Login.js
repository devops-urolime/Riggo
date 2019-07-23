import React, {Component} from 'react';
import './Login.scss';
import { WebAuth } from "auth0-js";
import { TextField, Button } from '@material-ui/core';
import { AUTH_CONFIG } from '../config';
import { LOGO_MAIN_LOGIN } from './Icon';
import Icon from './Icon';
import Grid from '@material-ui/core/Grid/Grid';

const webAuth = new WebAuth({
  domain: AUTH_CONFIG.domain,
  clientID: AUTH_CONFIG.clientId,
  redirectUri: AUTH_CONFIG.callbackUrl,
  audience: AUTH_CONFIG.audience,
  responseType: AUTH_CONFIG.responseType,
  scope: AUTH_CONFIG.scope
});

const showMessage = (msg) => {
    console.log({ description: msg });
  };
  const loginResult = (err) => {
      if (err && (err.code !== 400 )) {
          showMessage('Something went wrong: ' + err.message);
      }
      else if ( err && (err.code === 400)
      ) {
        showMessage("Please complete user and password.");
      }
  };
  const handleAuthentication = (email, password) => {
    webAuth.login({
      realm: 'Username-Password-Authentication',
      username: email,
      password: password,
    }, (err) => loginResult(err));
  };

const forgotPassword = () => {
  showMessage("We've just sent you an email to reset your password.");
};

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
      password:""
    };
  }

  onEmailChange = (evt) =>{
     this.setState({email: evt.target.value})
  };

  onPasswordChange = (evt) =>{
     this.setState({password: evt.target.value})
  };

  loginFormAction = (evt) => {
    evt.preventDefault();
    handleAuthentication(this.state.email, this.state.password);
  };

  render(){
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
                />
              </div>
            </Grid>
            <Grid item xs={gridWidth}>
              <div onClick={forgotPassword}  className="ForgotPassword" role="link" id="forgot-password">
                Forgot Password ?
              </div>
            </Grid>
            <Grid item xs={gridWidth}>
              <div className="ButtonGroup">
                <Button variant="contained" color="primary">
                  Sign In
                </Button>
              </div>
            </Grid>
          </form>
        </Grid>
      </div>
    );
  }
}

export default Login;
