import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import CodeTables from './code-tables';
import CodeTablesDetail from './code-tables-detail';
import CodeTablesUpdate from './code-tables-update';
import CodeTablesDeleteDialog from './code-tables-delete-dialog';

const CodeTablesRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<CodeTables />} />
    <Route path="new" element={<CodeTablesUpdate />} />
    <Route path=":id">
      <Route index element={<CodeTablesDetail />} />
      <Route path="edit" element={<CodeTablesUpdate />} />
      <Route path="delete" element={<CodeTablesDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default CodeTablesRoutes;
