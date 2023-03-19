export interface IRestaurant {
  id?: number;
  name?: string;
  address?: string;
}

export const defaultValue: Readonly<IRestaurant> = {};
