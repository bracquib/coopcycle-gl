import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Livreur from './livreur';
import LivreurDetail from './livreur-detail';
import LivreurUpdate from './livreur-update';
import LivreurDeleteDialog from './livreur-delete-dialog';

const LivreurRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Livreur />} />
    <Route path="new" element={<LivreurUpdate />} />
    <Route path=":id">
      <Route index element={<LivreurDetail />} />
      <Route path="edit" element={<LivreurUpdate />} />
      <Route path="delete" element={<LivreurDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default LivreurRoutes;
