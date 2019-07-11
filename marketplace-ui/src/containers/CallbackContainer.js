import { connect } from 'react-redux';
import { withRouter } from 'react-router-dom';
import Callback from '../components/Callback';
import { loginSuccess,loginFail } from '../redux/actions/auth';

const mapDispatchToProps = dispatch => {
  return {
    loginSuccess: (token) => {
      dispatch(loginSuccess(token));
    },
    loginFail: (err) => {
      dispatch(loginFail(err));
    },
  };
};

export default connect(
  null,
  mapDispatchToProps
)(withRouter(Callback));
