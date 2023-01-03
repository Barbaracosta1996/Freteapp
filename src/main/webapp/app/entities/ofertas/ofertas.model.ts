import dayjs from 'dayjs/esm';
import { IPerfil } from 'app/entities/perfil/perfil.model';
import { TipoCarga } from 'app/entities/enumerations/tipo-carga.model';
import { StatusOferta } from 'app/entities/enumerations/status-oferta.model';
import { TipoOferta } from 'app/entities/enumerations/tipo-oferta.model';
import { TipoTransporteOferta } from 'app/entities/enumerations/tipo-transporte-oferta.model';

export interface IOfertas {
  id: number;
  dataPostagem?: dayjs.Dayjs | null;
  quantidade?: number | null;
  tipoCarga?: TipoCarga | null;
  localizacaoOrigem?: string | null;
  localizacaoDestino?: string | null;
  dataColeta?: dayjs.Dayjs | null;
  dataEntrega?: dayjs.Dayjs | null;
  dataModificacao?: dayjs.Dayjs | null;
  dataFechamento?: any | null;
  status?: StatusOferta | null;
  tipoOferta?: TipoOferta | null;
  tipoTransporte?: TipoTransporteOferta | null;
  destino?: string | null;
  origem?: string | null;
  perfil?: IPerfil | null;
  perfilCompleto?: IPerfil | null;
  ofertaCorrente?: number | null;
}

export type NewOfertas = Omit<IOfertas, 'id'> & { id: null };
