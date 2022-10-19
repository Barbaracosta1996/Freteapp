import { IPerfil } from 'app/entities/perfil/perfil.model';
import { ICategoriaVeiculo } from 'app/entities/categoria-veiculo/categoria-veiculo.model';
import { TipoCategoria } from 'app/entities/enumerations/tipo-categoria.model';

export interface IFrota {
  id: number;
  nome?: string | null;
  modelo?: string | null;
  marca?: string | null;
  ano?: string | null;
  tipoCategoria?: TipoCategoria | null;
  obsCategoriaOutro?: string | null;
  perfil?: Pick<IPerfil, 'id'> | null;
  categoriaVeiculo?: Pick<ICategoriaVeiculo, 'id'> | null;
}

export type NewFrota = Omit<IFrota, 'id'> & { id: null };
