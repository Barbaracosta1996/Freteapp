import { IUser } from 'app/entities/user/user.model';
import { TipoConta } from 'app/entities/enumerations/tipo-conta.model';

export interface IPerfil {
  id: number;
  tipoConta?: TipoConta | null;
  cpf?: string | null;
  cnpj?: string | null;
  rua?: string | null;
  numero?: string | null;
  bairro?: string | null;
  cidade?: string | null;
  estado?: string | null;
  cep?: string | null;
  pais?: string | null;
  nome?: string | null;
  razaosocial?: string | null;
  telefoneComercial?: string | null;
  user?: Pick<IUser, 'id' | 'login'> | null;
}

export type NewPerfil = Omit<IPerfil, 'id'> & { id: null };
