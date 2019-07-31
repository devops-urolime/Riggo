import { connect } from 'react-redux';
import App from '../components/App';
import { isLogin } from '../redux/reducers/auth';

const mapStateToProps = state => {
  return {
    isLogin: isLogin(state)
  }
};

export default connect(
  mapStateToProps,
  null
)(App);

