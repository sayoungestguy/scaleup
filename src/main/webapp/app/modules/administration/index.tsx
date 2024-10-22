import React from 'react';

import { Route } from 'react-router-dom';
import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import UserManagement from './user-management';
import Logs from './logs/logs';
import Health from './health/health';
import Metrics from './metrics/metrics';
import Configuration from './configuration/configuration';
import Docs from './docs/docs';
import Skill from 'app/entities/skill/skill';
import UserSkill from 'app/entities/user-skill/user-skill';
import UserProfile from 'app/entities/user-profile/user-profile';

const AdministrationRoutes = () => (
  <div>
    <ErrorBoundaryRoutes>
      <Route path="user-management/*" element={<UserManagement />} />
      <Route path="health" element={<Health />} />
      <Route path="metrics" element={<Metrics />} />
      <Route path="configuration" element={<Configuration />} />
      <Route path="logs" element={<Logs />} />
      <Route path="docs" element={<Docs />} />
      <Route path="skill/*" element={<Skill />} />
      <Route path="user-skill/*" element={<UserSkill />} />
      <Route path="user-profile/*" element={<UserProfile />} />
    </ErrorBoundaryRoutes>
  </div>
);

export default AdministrationRoutes;
