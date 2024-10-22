import React from 'react';
import { faGraduationCap } from '@fortawesome/free-solid-svg-icons';
import { NavDropdown } from './menu-components';
import EntitiesMenuItems from 'app/entities/menu';

export const EntitiesMenu = () => (
  <NavDropdown icon={faGraduationCap} name="Learning Hub" id="entity-menu" data-cy="entity" style={{ maxHeight: '80vh', overflow: 'auto' }}>
    <EntitiesMenuItems />
  </NavDropdown>
);
