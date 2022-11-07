import dayjs from 'dayjs/esm';
import { IOfertas } from 'app/entities/ofertas/ofertas.model';
import { IPerfil } from 'app/entities/perfil/perfil.model';
import { AnwserStatus } from 'app/entities/enumerations/anwser-status.model';
import { StatusSolicitacao } from 'app/entities/enumerations/status-solicitacao.model';

export interface ISolicitacao {
  id: number;
  dataProposta?: dayjs.Dayjs | null;
  dataModificacao?: dayjs.Dayjs | null;
  aceitar?: AnwserStatus | null;
  status?: StatusSolicitacao | null;
  obs?: string | null;
  valorFrete?: number | null;
  ofertas?: Pick<IOfertas, 'id'> | null;
  perfil?: Pick<IPerfil, 'id'> | null;
  minhaOferta?: Pick<IOfertas, 'id'> | null;
  requestedPerfil?: Pick<IPerfil, 'id'> | null;
}

export type NewSolicitacao = Omit<ISolicitacao, 'id'> & { id: null };
