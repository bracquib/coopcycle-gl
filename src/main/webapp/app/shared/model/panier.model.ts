import { IClient } from 'app/shared/model/client.model';
import { ICommande } from 'app/shared/model/commande.model';

export interface IPanier {
  id?: number;
  client?: IClient | null;
  commande?: ICommande | null;
}

export const defaultValue: Readonly<IPanier> = {};
