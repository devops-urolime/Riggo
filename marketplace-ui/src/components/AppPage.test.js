import App from './AppPage';
import TopBar from './TopBar';
import SideBar from '../components/SideBar';
import React from 'react';
import { BrowserRouter } from 'react-router-dom';

describe('<App />', () => {

  const baseTestProps = {
    auth:{
      renewSession: () => {} ,
      isAuthenticated: () => {}
    },
    loadMenu: () => {}
  };

  const wrapperBuilder = (testProps) => {
    return renderer
      .create(
        <BrowserRouter>
          <App {...testProps} />
        </BrowserRouter>
      );
  };

  const wrapperMount = (testProps) => {
      return mount(
          <BrowserRouter>
            <App {...testProps} />
          </BrowserRouter>
        );
    };

  it('renders correctly', () => {
    const tree = wrapperBuilder(baseTestProps).toJSON();
    expect(tree).toMatchSnapshot();
  });

  it('must contain one TopBar component', () => {
    const wrapper = wrapperMount(baseTestProps);
    expect_c(wrapper.find(TopBar)).to.have.lengthOf(1);
  });

  it('must contain one SideBar component', () => {
    const wrapper = wrapperMount(baseTestProps);
    expect_c(wrapper.find(SideBar)).to.have.lengthOf(1);
  });

  it('Should load menus on componentDidMount', () => {
      const loadMenu = sinon.spy();
      const propsTest = {
        ...baseTestProps,
        loadMenu
      };
      wrapperMount(propsTest);
      expect_c(loadMenu).to.have.property('callCount', 1);
    });

});
