import dayjs from 'dayjs/esm';
import { WhatsAppType } from 'app/entities/enumerations/whats-app-type.model';
import { WhatsStatus } from 'app/entities/enumerations/whats-status.model';
import { TipoOferta } from 'app/entities/enumerations/tipo-oferta.model';

export interface IWhatsMessageBatch {
  id: number;
  tipo?: WhatsAppType | null;
  waidTo?: string | null;
  perfilID?: number | null;
  status?: WhatsStatus | null;
  ofertaId?: number | null;
  tipoOferta?: TipoOferta | null;
  notificationDate?: dayjs.Dayjs | null;
  createdDate?: dayjs.Dayjs | null;
}

export type NewWhatsMessageBatch = Omit<IWhatsMessageBatch, 'id'> & { id: null };
