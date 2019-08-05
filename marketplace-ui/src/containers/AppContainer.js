import { connect } from 'react-redux';
import App from '../components/App';
import { isLogin } from '../redux/reducers/auth';
import {
  EXPIRES_IN_LOCAL_STORAGE,
  IS_LOGGED_IN_LOCAL_STORAGE,
  isNotExpired,
  JWT_LOCAL_STORAGE
} from '../lib/auth';
import { loginSuccess } from '../redux/actions/auth';

const mapStateToProps = state => {
  return {
    isLogin: isLogin(state)
  }
};

const mapDispatchToProps = dispatch => {
  return {
    isAlreadyLocallyAuthenticated: () => {
      const isLoggedInLocal = localStorage.getItem(IS_LOGGED_IN_LOCAL_STORAGE) === 'true';
      const expiresIn = localStorage.getItem(EXPIRES_IN_LOCAL_STORAGE);
      if (
        isLoggedInLocal  &&
        isNotExpired(expiresIn)
      ) {
        const JWT = localStorage.getItem(JWT_LOCAL_STORAGE);
        if(JWT && expiresIn){
          dispatch(loginSuccess(JWT, expiresIn));
        }
      }

    }
  };
};

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(App);

