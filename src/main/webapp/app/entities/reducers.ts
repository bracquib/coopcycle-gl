import client from 'app/entities/client/client.reducer';
import restaurant from 'app/entities/restaurant/restaurant.reducer';
import livreur from 'app/entities/livreur/livreur.reducer';
import societaire from 'app/entities/societaire/societaire.reducer';
import commande from 'app/entities/commande/commande.reducer';
import panier from 'app/entities/panier/panier.reducer';
import plateformePaiement from 'app/entities/plateforme-paiement/plateforme-paiement.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  client,
  restaurant,
  livreur,
  societaire,
  commande,
  panier,
  plateformePaiement,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
