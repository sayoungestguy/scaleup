import './footer.scss';
import React, { useState } from 'react';
import { Col, Row, Container, Button, Input } from 'reactstrap';

const Footer = () => {
  const [email, setEmail] = useState('');

  const handleSubscribe = () => {
    if (email) {
      alert(`Subscribed with ${email}`);
      setEmail('');
    } else {
      alert('Please enter a valid email.');
    }
  };

  return (
    <footer className="footer footer-dark">
      <Container>
        <Row>
          <Col md="3" sm="12" className="text-center mb-3">
            <h5>ScaleUp</h5>
            <ul className="list-unstyled">
              <li>
                <a href="/about">About Us</a>
              </li>
              <li>
                <a href="/contact">Contact</a>
              </li>
            </ul>
          </Col>
          <Col md="3" sm="12" className="text-center mb-3">
            <h5>Follow Us</h5>
            <ul className="list-unstyled">
              <li>
                <a href="https://facebook.com">Facebook</a>
              </li>
              <li>
                <a href="https://twitter.com">Twitter</a>
              </li>
              <li>
                <a href="https://instagram.com">Instagram</a>
              </li>
            </ul>
          </Col>
          <Col md="3" sm="12" className="text-center mb-3">
            <h5>Support</h5>
            <ul className="list-unstyled">
              <li>
                <a href="/faq">FAQ</a>
              </li>
              <li>
                <a href="/support">Help Center</a>
              </li>
            </ul>
          </Col>
          <Col md="3" sm="12" className="text-center mb-3">
            <h5>Subscribe to Our ScaleUp Newsletter</h5>
            <Input type="email" value={email} placeholder="Enter your email" onChange={e => setEmail(e.target.value)} className="mb-2" />
            <Button color="primary" onClick={handleSubscribe}>
              Subscribe
            </Button>
          </Col>
        </Row>
        <Row>
          <Col md="12" className="text-center mt-4">
            <p>&copy; {new Date().getFullYear()} ScaleUp. All rights reserved.</p>
          </Col>
        </Row>
      </Container>
    </footer>
  );
};

export default Footer;
