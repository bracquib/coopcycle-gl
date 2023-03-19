export interface ILivreur {
  id?: number;
  name?: string;
  surname?: string;
  email?: string;
  phonenumber?: string | null;
  vehicleType?: string;
  commandState?: string;
}

export const defaultValue: Readonly<ILivreur> = {};
