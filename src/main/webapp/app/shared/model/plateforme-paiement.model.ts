import { ICommande } from 'app/shared/model/commande.model';
import { TypePaiement } from 'app/shared/model/enumerations/type-paiement.model';

export interface IPlateformePaiement {
  id?: number;
  amount?: string;
  paymentType?: TypePaiement;
  description?: string | null;
  commande?: ICommande | null;
}

export const defaultValue: Readonly<IPlateformePaiement> = {};
