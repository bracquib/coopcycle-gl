import { IClient } from 'app/shared/model/client.model';
import { IRestaurant } from 'app/shared/model/restaurant.model';
import { ILivreur } from 'app/shared/model/livreur.model';

export interface ICommande {
  id?: number;
  creationDate?: string;
  deliveryDate?: string | null;
  status?: string;
  client?: IClient | null;
  restaurant?: IRestaurant | null;
  livreur?: ILivreur | null;
}

export const defaultValue: Readonly<ICommande> = {};
