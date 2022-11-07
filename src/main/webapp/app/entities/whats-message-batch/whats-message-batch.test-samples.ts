import dayjs from 'dayjs/esm';

import { WhatsAppType } from 'app/entities/enumerations/whats-app-type.model';
import { WhatsStatus } from 'app/entities/enumerations/whats-status.model';
import { TipoOferta } from 'app/entities/enumerations/tipo-oferta.model';

import { IWhatsMessageBatch, NewWhatsMessageBatch } from './whats-message-batch.model';

export const sampleWithRequiredData: IWhatsMessageBatch = {
  id: 96663,
  tipo: WhatsAppType['LIST_ALERT'],
  waidTo: 'focus bordô',
  perfilID: 98105,
  status: WhatsStatus['CLOSED'],
};

export const sampleWithPartialData: IWhatsMessageBatch = {
  id: 26563,
  tipo: WhatsAppType['CANCELED_TRAVEL'],
  waidTo: 'Camiseta Gostoso',
  perfilID: 18619,
  status: WhatsStatus['CLOSED'],
  ofertaId: 78246,
  tipoOferta: TipoOferta['CARGA'],
  notificationDate: dayjs('2022-11-02T22:21'),
  createdDate: dayjs('2022-11-02T17:12'),
};

export const sampleWithFullData: IWhatsMessageBatch = {
  id: 21907,
  tipo: WhatsAppType['INDICATION_ALERT'],
  waidTo: 'Industrial circuit',
  perfilID: 30378,
  status: WhatsStatus['CANCELED'],
  ofertaId: 23286,
  tipoOferta: TipoOferta['CARGA'],
  notificationDate: dayjs('2022-11-02T02:03'),
  createdDate: dayjs('2022-11-02T18:38'),
};

export const sampleWithNewData: NewWhatsMessageBatch = {
  tipo: WhatsAppType['LIST_MATCH_APPROVED'],
  waidTo: 'client-server',
  perfilID: 11273,
  status: WhatsStatus['CANCELED'],
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
