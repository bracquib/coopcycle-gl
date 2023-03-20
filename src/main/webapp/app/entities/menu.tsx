import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/client">
        <Translate contentKey="global.menu.entities.client" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/restaurant">
        <Translate contentKey="global.menu.entities.restaurant" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/livreur">
        <Translate contentKey="global.menu.entities.livreur" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/societaire">
        <Translate contentKey="global.menu.entities.societaire" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/commande">
        <Translate contentKey="global.menu.entities.commande" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/panier">
        <Translate contentKey="global.menu.entities.panier" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/plateforme-paiement">
        <Translate contentKey="global.menu.entities.plateformePaiement" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
