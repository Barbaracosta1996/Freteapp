import dayjs from 'dayjs/esm';
import { IPerfil } from 'app/entities/perfil/perfil.model';
import { TipoPerfilDocumento } from 'app/entities/enumerations/tipo-perfil-documento.model';

export interface IPerfilAnexos {
  id: number;
  dataPostagem?: dayjs.Dayjs | null;
  tipoDocumento?: TipoPerfilDocumento | null;
  descricao?: string | null;
  urlFile?: string | null;
  urlFileContentType?: string | null;
  perfil?: Pick<IPerfil, 'id'> | null;
}

export type NewPerfilAnexos = Omit<IPerfilAnexos, 'id'> & { id: null };
