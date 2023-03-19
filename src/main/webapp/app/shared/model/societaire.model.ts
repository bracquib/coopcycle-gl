import { IClient } from 'app/shared/model/client.model';
import { IRestaurant } from 'app/shared/model/restaurant.model';
import { ILivreur } from 'app/shared/model/livreur.model';

export interface ISocietaire {
  id?: number;
  client?: string;
  restaurant?: string | null;
  livreur?: string | null;
  client?: IClient | null;
  restaurant?: IRestaurant | null;
  livreur?: ILivreur | null;
}

export const defaultValue: Readonly<ISocietaire> = {};
