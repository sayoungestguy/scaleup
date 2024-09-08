import './footer.scss';
import React from 'react';
import { Col, Row } from 'reactstrap';

const Footer = () => (
  <footer className="footer">
    <Row>
      <Col md="12" className="text-center">
        <p>&copy; {new Date().getFullYear()} Scaleup. All rights reserved.</p>
      </Col>
    </Row>
  </footer>
);

export default Footer;
