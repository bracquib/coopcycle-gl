export interface IClient {
  id?: number;
  name?: string;
  surname?: string;
  email?: string;
  phonenumber?: string | null;
  address?: string;
}

export const defaultValue: Readonly<IClient> = {};
