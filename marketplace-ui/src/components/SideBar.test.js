import SideBar from './SideBar';
import React from 'react';
import MenuContent from './MenuContent';
import Drawer from '@material-ui/core/Drawer';
import { BrowserRouter } from 'react-router-dom';

describe('<SideBar />', () => {
  const baseTestProps = {
    isOpen: false,
    handleClose: () => {},
    menu: [],
    defaultMenu: {},
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
      <SideBar {...testProps} />
    );
  };

  it('renders correctly', () => {
    const tree = wrapperBuilder(baseTestProps).toJSON();
    expect(tree).toMatchSnapshot();
  });

  it('must contain one MenuContent component for menus items', () => {
    const wrapper = wrapperShallow(baseTestProps);
    expect_c(wrapper.find(MenuContent)).to.have.lengthOf(1);
  });

  it('must contain one Drawer component to support show and hide menu', () => {
    const wrapper = wrapperShallow(baseTestProps);
    expect_c(wrapper.find(Drawer)).to.have.lengthOf(1);
  });

});
