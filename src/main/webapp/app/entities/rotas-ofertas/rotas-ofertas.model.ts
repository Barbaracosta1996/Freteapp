import { IOfertas } from 'app/entities/ofertas/ofertas.model';

export interface IRotasOfertas {
  id: number;
  rotas?: string | null;
  ofertas?: Pick<IOfertas, 'id'> | null;
}

export type NewRotasOfertas = Omit<IRotasOfertas, 'id'> & { id: null };
