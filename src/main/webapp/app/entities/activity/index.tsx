import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Activity from './activity';
import ActivityDetail from './activity-detail';
import ActivityUpdate from './activity-update';
import ActivityDeleteDialog from './activity-delete-dialog';

const ActivityRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Activity />} />
    <Route path="new" element={<ActivityUpdate />} />
    <Route path=":id">
      <Route index element={<ActivityDetail />} />
      <Route path="edit" element={<ActivityUpdate />} />
      <Route path="delete" element={<ActivityDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ActivityRoutes;
