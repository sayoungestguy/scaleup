import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import CodeTables from './code-tables';
import UserProfile from './user-profile';
import Skill from './skill';
import Message from './message';
import Activity from './activity';
import ActivityInvite from './activity-invite';
import Notification from './notification';
import UserSkill from './user-skill';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="code-tables/*" element={<CodeTables />} />
        <Route path="user-profile/*" element={<UserProfile />} />
        <Route path="skill/*" element={<Skill />} />
        <Route path="message/*" element={<Message />} />
        <Route path="activity/*" element={<Activity />} />
        <Route path="activity-invite/*" element={<ActivityInvite />} />
        <Route path="notification/*" element={<Notification />} />
        <Route path="user-skill/*" element={<UserSkill />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
