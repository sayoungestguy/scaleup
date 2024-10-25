import React, { useEffect } from 'react';

import MenuItem from 'app/shared/layout/menus/menu-item';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities } from 'app/modules/account/profile/user-profile.reducer';
import { IUserProfile } from 'app/shared/model/user-profile.model';

const EntitiesMenu = () => {
  const dispatchEntities = useAppDispatch();
  const account = useAppSelector(state => state.authentication.account); // Get account information
  const userProfileEntitties = useAppSelector(state => state.userProfile.entities);

  useEffect(() => {
    dispatchEntities(
      getEntities({
        query: `createdBy.equals=${account.login}`,
      }),
    );
  }, []);

  const loggedInUserProfile = userProfileEntitties.find((profile: IUserProfile) => profile.createdBy === account.login);

  // if user doesn't have a user profile created, the profile option will route to the create a new profile page
  if (!loggedInUserProfile?.id) {
    return (
      <>
        {/* prettier-ignore */}
        <MenuItem icon="book" to="/user-profile/new">
          Activity
        </MenuItem>
        <MenuItem icon="people-arrows" to="/user-profile/new">
          Activity Invite
        </MenuItem>
        <MenuItem icon="envelope" to="/user-profile/new">
          Message
        </MenuItem>
        <MenuItem icon="search" to="/user-profile/new">
          Search ScaleUp Network
        </MenuItem>
      </>
    );
  }

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
