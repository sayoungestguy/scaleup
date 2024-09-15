import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import ActivityInvite from './activity-invite';
import ActivityInviteDetail from './activity-invite-detail';
import ActivityInviteUpdate from './activity-invite-update';
import ActivityInviteDeleteDialog from './activity-invite-delete-dialog';

const ActivityInviteRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<ActivityInvite />} />
    <Route path="new" element={<ActivityInviteUpdate />} />
    <Route path=":id">
      <Route index element={<ActivityInviteDetail />} />
      <Route path="edit" element={<ActivityInviteUpdate />} />
      <Route path="delete" element={<ActivityInviteDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ActivityInviteRoutes;
