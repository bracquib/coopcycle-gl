import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Client from './client';
import Restaurant from './restaurant';
import Livreur from './livreur';
import Societaire from './societaire';
import Commande from './commande';
import Panier from './panier';
import PlateformePaiement from './plateforme-paiement';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="client/*" element={<Client />} />
        <Route path="restaurant/*" element={<Restaurant />} />
        <Route path="livreur/*" element={<Livreur />} />
        <Route path="societaire/*" element={<Societaire />} />
        <Route path="commande/*" element={<Commande />} />
        <Route path="panier/*" element={<Panier />} />
        <Route path="plateforme-paiement/*" element={<PlateformePaiement />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
