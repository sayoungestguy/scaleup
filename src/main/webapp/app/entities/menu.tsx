import React, { useEffect } from 'react';

import MenuItem from 'app/shared/layout/menus/menu-item';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities } from 'app/modules/account/profile/user-profile.reducer';
import { IUserProfile } from 'app/shared/model/user-profile.model';
import { getAllUserProfiles } from 'app/entities/user-profile/user-profile.reducer';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="book" to="/activity">
        Activity
      </MenuItem>
      <MenuItem icon="people-arrows" to="/activity-invite">
        Activity Invite
      </MenuItem>
      <MenuItem icon="envelope" to="/message">
        Message
      </MenuItem>
      <MenuItem icon="search" to="/search">
        Search ScaleUp Network
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
