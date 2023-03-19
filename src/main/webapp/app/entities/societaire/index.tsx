import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Societaire from './societaire';
import SocietaireDetail from './societaire-detail';
import SocietaireUpdate from './societaire-update';
import SocietaireDeleteDialog from './societaire-delete-dialog';

const SocietaireRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Societaire />} />
    <Route path="new" element={<SocietaireUpdate />} />
    <Route path=":id">
      <Route index element={<SocietaireDetail />} />
      <Route path="edit" element={<SocietaireUpdate />} />
      <Route path="delete" element={<SocietaireDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default SocietaireRoutes;
