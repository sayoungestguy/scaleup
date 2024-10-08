import React, { useState } from 'react';
import {
  Collapse,
  Navbar,
  NavbarToggler,
  Nav,
  NavItem,
  NavLink,
  NavbarBrand,
  DropdownToggle,
  DropdownMenu,
  DropdownItem,
  UncontrolledDropdown,
} from 'reactstrap';
import { NavLink as Link } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

export const BrandIcon = props => (
  <div {...props} className="brand-icon">
    <img src="content/images/companypic.png" alt="Logo" />
  </div>
);

export const Brand = () => (
  <NavbarBrand tag={Link} to="/" className="brand-logo">
    <BrandIcon />
    <h1>
      <span className="brand-title">ScaleUp</span>
    </h1>
    {/* <span className="navbar-version">{VERSION.toLowerCase().startsWith('v') ? VERSION : `v${VERSION}`}</span> */}
  </NavbarBrand>
);

export const Home = () => (
  <NavItem>
    <NavLink tag={Link} to="/" className="d-flex align-items-center">
      <FontAwesomeIcon icon="home" />
      <span className="ml-2">Home</span>
    </NavLink>
  </NavItem>
);

export const Header = () => {
  const [isOpen, setIsOpen] = useState(false);

  const toggle = () => setIsOpen(!isOpen);

  return (
    <Navbar color="dark" dark expand="md" className="mb-4">
      <Brand />
      <NavbarToggler onClick={toggle} />
      <Collapse isOpen={isOpen} navbar>
        <Nav className="ml-auto" navbar>
          <Home />
          <NavItem>
            <NavLink tag={Link} to="/about" className="d-flex align-items-center">
              <FontAwesomeIcon icon="info-circle" />
              <span className="ml-2">About</span>
            </NavLink>
          </NavItem>
          <NavItem>
            <NavLink tag={Link} to="/contact" className="d-flex align-items-center">
              <FontAwesomeIcon icon="envelope" />
              <span className="ml-2">Contact</span>
            </NavLink>
          </NavItem>
          <UncontrolledDropdown nav inNavbar>
            <DropdownToggle nav caret className="d-flex align-items-center">
              <FontAwesomeIcon icon="user" />
              <span className="ml-2">Account</span>
            </DropdownToggle>
            <DropdownMenu right>
              <DropdownItem tag={Link} to="/profile">
                Profile
              </DropdownItem>
              <DropdownItem tag={Link} to="/settings">
                Settings
              </DropdownItem>
              <DropdownItem divider />
              <DropdownItem tag={Link} to="/logout">
                Logout
              </DropdownItem>
            </DropdownMenu>
          </UncontrolledDropdown>
        </Nav>
      </Collapse>
    </Navbar>
  );
};
