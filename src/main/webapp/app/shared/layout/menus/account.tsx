import React, { useEffect } from 'react';
import MenuItem from 'app/shared/layout/menus/menu-item';
import { useAppDispatch, useAppSelector } from 'app/config/store'; // Import the selector to get account

import { NavDropdown } from './menu-components';
import { getEntities, getEntityByCreatedBy } from 'app/modules/account/profile/user-profile.reducer';
import { IUserProfile } from 'app/shared/model/user-profile.model';

const accountMenuItemsAuthenticated = () => {
  const dispatch = useAppDispatch();
  const account = useAppSelector(state => state.authentication.account); // Get account information
  const userProfileEntitties = useAppSelector(state => state.userProfile.entities);

  useEffect(() => {
    dispatch(
      getEntities({
        query: `createdBy.equals=${account.login}`,
      }),
    );
  }, []);

  const loggedInUserProfile = userProfileEntitties.find((profile: IUserProfile) => profile.createdBy === account.login);

  // const loggedInUserProfile = userProfileEntitties.find(
  //   (profile) => profile.createdBy === account.login
  // );

  return (
    <>
      {/* <MenuItem icon="asterisk" to={`/account/profile/${account.login}`} data-cy="profile">
        Profile
      </MenuItem> */}

      {/* Check if loggedInUserProfile exists and route to that profile */}
      {loggedInUserProfile && (
        <MenuItem icon="asterisk" to={`/user-profile/${loggedInUserProfile.id}`} data-cy="profile">
          Profile
        </MenuItem>
      )}

      <MenuItem icon="wrench" to="/account/settings" data-cy="settings">
        Settings
      </MenuItem>
      <MenuItem icon="lock" to="/account/password" data-cy="passwordItem">
        Password
      </MenuItem>
      <MenuItem icon="sign-out-alt" to="/logout" data-cy="logout">
        Sign out
      </MenuItem>
    </>
  );

  // <>
  //   <MenuItem icon="asterisk" to={`/account/profile/${account.login}`} data-cy="profile">
  //     Profile
  //   </MenuItem>

  //   <MenuItem icon="asterisk" to={`/user-profile/${userProfileEntity.id}`} data-cy="profile">
  //     User Profile
  //   </MenuItem>

  //   <MenuItem icon="wrench" to="/account/settings" data-cy="settings">
  //     Settings
  //   </MenuItem>
  //   <MenuItem icon="lock" to="/account/password" data-cy="passwordItem">
  //     Password
  //   </MenuItem>
  //   <MenuItem icon="sign-out-alt" to="/logout" data-cy="logout">
  //     Sign out
  //   </MenuItem>
  // </>
};

const accountMenuItems = () => (
  <>
    <MenuItem id="login-item" icon="sign-in-alt" to="/login" data-cy="login">
      Sign in
    </MenuItem>
    <MenuItem icon="user-plus" to="/account/register" data-cy="register">
      Register
    </MenuItem>
  </>
);

export const AccountMenu = ({ isAuthenticated = false }) => (
  <NavDropdown icon="user" name="Account" id="account-menu" data-cy="accountMenu">
    {isAuthenticated && accountMenuItemsAuthenticated()}
    {!isAuthenticated && accountMenuItems()}
  </NavDropdown>
);

export default AccountMenu;
