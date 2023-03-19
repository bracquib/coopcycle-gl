import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import PlateformePaiement from './plateforme-paiement';
import PlateformePaiementDetail from './plateforme-paiement-detail';
import PlateformePaiementUpdate from './plateforme-paiement-update';
import PlateformePaiementDeleteDialog from './plateforme-paiement-delete-dialog';

const PlateformePaiementRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<PlateformePaiement />} />
    <Route path="new" element={<PlateformePaiementUpdate />} />
    <Route path=":id">
      <Route index element={<PlateformePaiementDetail />} />
      <Route path="edit" element={<PlateformePaiementUpdate />} />
      <Route path="delete" element={<PlateformePaiementDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default PlateformePaiementRoutes;
