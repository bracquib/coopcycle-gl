import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Panier from './panier';
import PanierDetail from './panier-detail';
import PanierUpdate from './panier-update';
import PanierDeleteDialog from './panier-delete-dialog';

const PanierRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Panier />} />
    <Route path="new" element={<PanierUpdate />} />
    <Route path=":id">
      <Route index element={<PanierDetail />} />
      <Route path="edit" element={<PanierUpdate />} />
      <Route path="delete" element={<PanierDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default PanierRoutes;
