import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Commande from './commande';
import CommandeDetail from './commande-detail';
import CommandeUpdate from './commande-update';
import CommandeDeleteDialog from './commande-delete-dialog';

const CommandeRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Commande />} />
    <Route path="new" element={<CommandeUpdate />} />
    <Route path=":id">
      <Route index element={<CommandeDetail />} />
      <Route path="edit" element={<CommandeUpdate />} />
      <Route path="delete" element={<CommandeDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default CommandeRoutes;
