import React from 'react';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      {/*<MenuItem icon="asterisk" to="/code-tables">*/}
      {/*  Code Tables*/}
      {/*</MenuItem>*/}
      <MenuItem icon="user" to="/user-profile">
        User Profile
      </MenuItem>
      <MenuItem icon="pencil-alt" to="/skill">
        Skill
      </MenuItem>
      <MenuItem icon="envelope" to="/message">
        Message
      </MenuItem>
      <MenuItem icon="book" to="/activity">
        Activity
      </MenuItem>
      <MenuItem icon="people-arrows" to="/activity-invite">
        Activity Invite
      </MenuItem>
      {/*<MenuItem icon="asterisk" to="/notification">*/}
      {/*  Notification*/}
      {/*</MenuItem>*/}
      <MenuItem icon="asterisk" to="/user-skill">
        User Skill
      </MenuItem>
      <MenuItem icon="search" to="/search">
        Search ScaleUp Network
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
