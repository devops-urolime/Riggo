import App from './App';
import { Switch, Route, BrowserRouter } from 'react-router-dom';
import React from 'react';
import { APP_PATH_AUTH0_CALLBACK, APP_PATH_LOGIN, APP_PATH_ROOT } from '../config';
import SideBar from './SideBar';

describe('<App />', () => {

  afterAll(() => process.exit());

  const baseTestProps = {
    auth:{
      renewSession: () => {} ,
      isAuthenticated: () => {}
    }
  };

  const wrapperBuilder = (testProps) => {
    return renderer
      .create(
        <BrowserRouter>
          <SideBar {...testProps} />
        </BrowserRouter>
      );
  };
  const wrapperShallow = (testProps) => {
    return shallow(
      <App {...testProps} />
    );
  };

  it('renders correctly', () => {
    const tree = wrapperBuilder(baseTestProps).toJSON();
    expect(tree).toMatchSnapshot();
  });

  it('must contain one Switch component for routes', () => {
    const wrapper = wrapperShallow(baseTestProps);
    expect_c(wrapper.find(Switch)).to.have.lengthOf(1);
  });

  it('must contain 3 Route components for main navigation', () => {
      const wrapper = wrapperShallow(baseTestProps);
      expect_c(wrapper.find(Route)).to.have.lengthOf(3);
  });

  it('should support root navigation', () => {
      const wrapper = wrapperShallow(baseTestProps);
      const routeComponents = wrapper.find(Route).findWhere(
        n => n.props().path === APP_PATH_ROOT
      );
      expect_c(routeComponents).to.have.lengthOf(1);
  });

  it('should support login navigation', () => {
      const wrapper = wrapperShallow(baseTestProps);
      const routeComponents = wrapper.find(Route).findWhere(
        n => n.props().path === APP_PATH_LOGIN
      );
      expect_c(routeComponents).to.have.lengthOf(1);
  });

  it('should support callback url to get JWT for Auth 2.0 protocol', () => {
      const wrapper = wrapperShallow(baseTestProps);
      const routeComponents = wrapper.find(Route).findWhere(
        n => n.props().path === APP_PATH_AUTH0_CALLBACK
      );
      expect_c(routeComponents).to.have.lengthOf(1);
  });


});
