import React,{Component} from 'react';
import PropTypes from 'prop-types';

class LoadPage extends Component {
  componentDidMount() {
    const { loadId, findLoad } = this.props;
    findLoad(loadId);
  }
  render(){
    const { load, loadId } = this.props;
    const { sfId, expectedDeliveryDate, expectedShipDate } = load || {};
    return (
        <section>
            <h4>Load ID {loadId}</h4>
            <p>Sales Force ID: {sfId}. </p>
            <p>Delivery Date: {expectedDeliveryDate}. </p>
            <p>Ship Date: {expectedShipDate}. </p>
        </section>
    );
  }
}

LoadPage.propTypes = {
  loadId: PropTypes.string,
  findLoad: PropTypes.func,
  load: PropTypes.object,
};

export default LoadPage;
