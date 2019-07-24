import App from './App';
import { Switch, Route } from 'react-router-dom';
import React from 'react';
import { APP_PATH_AUTH0_CALLBACK, APP_PATH_LOGIN, APP_PATH_ROOT } from '../config';

describe('<App />', () => {
  const baseTestProps = {
    auth:{
      renewSession: () => {} ,
      isAuthenticated: () => {}
    }
  };

  const wrapperShallow = (testProps) => {
    return shallow(
      <App {...testProps} />
    );
  };

  it('renders correctly', () => {
    const tree = wrapperShallow(baseTestProps);
    expect(tree).toMatchSnapshot();
  });

  it('must contain one Switch component for routes', () => {
    const wrapper = shallow(<App {...baseTestProps} />);
    expect_c(wrapper.find(Switch)).to.have.lengthOf(1);
  });

  it('must contain 3 Route components for main navigation', () => {
      const wrapper = shallow(<App {...baseTestProps} />);
      expect_c(wrapper.find(Route)).to.have.lengthOf(3);
  });

  it('should support root navigation', () => {
      const wrapper = shallow(<App {...baseTestProps} />);
      const routeComponents = wrapper.find(Route).findWhere(
        n => n.props().path === APP_PATH_ROOT
      );
      expect_c(routeComponents).to.have.lengthOf(1);
  });

  it('should support login navigation', () => {
      const wrapper = shallow(<App {...baseTestProps} />);
      const routeComponents = wrapper.find(Route).findWhere(
        n => n.props().path === APP_PATH_LOGIN
      );
      expect_c(routeComponents).to.have.lengthOf(1);
  });

  it('should support callback url to get JWT for Auth 2.0 protocol', () => {
      const wrapper = shallow(<App {...baseTestProps} />);
      const routeComponents = wrapper.find(Route).findWhere(
        n => n.props().path === APP_PATH_AUTH0_CALLBACK
      );
      expect_c(routeComponents).to.have.lengthOf(1);
  });


});
