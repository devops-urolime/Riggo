import App from './App';
import TopBar from './TopBar';
import SideBar from '../containers/SideBarContainer';
import MainContent from './MainContent';

describe('<App />', () => {

  it('renders correctly', () => {
    const tree = renderer
      .create(<App auth={{renewSession: () => {} , isAuthenticated: () => {}}} />)
      .toJSON();
    expect(tree).toMatchSnapshot();
  });

  it('renders an `.App-layout`', () => {
    const wrapper = shallow(<App auth={{renewSession: () => {} }}/>);
    expect_c(wrapper.find('.App-layout')).to.have.lengthOf(1);
  });

  it('must contain one TopBar component', () => {
    const wrapper = shallow(<App auth={{renewSession: () => {} }}/>);
    expect_c(wrapper.find(TopBar)).to.have.lengthOf(1);
  });

  it('must contain one SideBar component', () => {
    const wrapper = shallow(<App auth={{renewSession: () => {} }}/>);
    expect_c(wrapper.find(SideBar)).to.have.lengthOf(1);
  });

  it('must contain one MainContent component', () => {
    const wrapper = shallow(<App auth={{renewSession: () => {} }}/>);
    expect_c(wrapper.find(MainContent)).to.have.lengthOf(1);
  });

  it('if the user is no login not call renewSession from auth object', () => {
    const onButtonClick = sinon.spy();
    const wrapper = shallow(<App auth={{renewSession: () => onButtonClick() }} />);
    wrapper.find(TopBar).simulate('click');
    expect_c(onButtonClick).to.have.property('callCount', 0);
  });

});
