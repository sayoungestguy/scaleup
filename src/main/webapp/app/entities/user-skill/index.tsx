import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import UserSkill from './user-skill';
import UserSkillDetail from './user-skill-detail';
import UserSkillUpdate from './user-skill-update';
import UserSkillDeleteDialog from './user-skill-delete-dialog';

const UserSkillRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<UserSkill />} />
    <Route path="new" element={<UserSkillUpdate />} />
    <Route path=":id">
      <Route index element={<UserSkillDetail />} />
      <Route path="edit" element={<UserSkillUpdate />} />
      <Route path="delete" element={<UserSkillDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default UserSkillRoutes;
