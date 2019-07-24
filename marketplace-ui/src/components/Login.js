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
import Paper from '@material-ui/core/Paper';

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

  toggleLogin = ()=> {
    this.setState(prevState => ({
      isSingUp: !prevState.isSingUp
    }));
  };

  render(){
    const { isOpenMessage, message, email, isSingUp }= this.state;
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
                        {
                          (!isSingUp) ?
                            "Log In":
                            "Sing Up"
                        }
                      </Button>
                    </div>
                  </Grid>
                  <Grid item xs={gridWidth}>
                    <div className="ButtonGroup">
                      <p className="SignUp" role="link" onClick={() => this.toggleLogin()} >
                        {
                          (!isSingUp) ?
                            "Don't have an account? Sign Up":
                            "I already have an account"
                        }
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

export default Login;
