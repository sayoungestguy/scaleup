import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import { Route } from 'react-router-dom';
import React from 'react';
import SearchAppActivity from 'app/entities/search/searchAppActivity';

const SearchAppActivityRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<SearchAppActivity />} />
  </ErrorBoundaryRoutes>
);

export default SearchAppActivityRoutes;
