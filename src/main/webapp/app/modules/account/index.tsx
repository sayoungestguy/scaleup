import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Settings from './settings/settings';
import Password from './password/password';
import ProfilePage from './profile/profile';

const AccountRoutes = () => (
  <div>
    <ErrorBoundaryRoutes>
      <Route path="profile/:username" element={<ProfilePage />} />
      <Route path="settings" element={<Settings />} />
      <Route path="password" element={<Password />} />
    </ErrorBoundaryRoutes>
  </div>
);

export default AccountRoutes;
