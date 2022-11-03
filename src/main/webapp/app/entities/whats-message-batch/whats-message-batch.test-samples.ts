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
  id: 56221,
  tipo: WhatsAppType['LIST_MATCH_APPROVED'],
  waidTo: 'Global',
  perfilID: 40744,
  status: WhatsStatus['CANCELED'],
  ofertaId: 10817,
  tipoOferta: TipoOferta['VAGAS'],
};

export const sampleWithFullData: IWhatsMessageBatch = {
  id: 81391,
  tipo: WhatsAppType['LIST_ALERT'],
  waidTo: 'primary one-to-one',
  perfilID: 42407,
  status: WhatsStatus['OPEN'],
  ofertaId: 98120,
  tipoOferta: TipoOferta['CARGA'],
};

export const sampleWithNewData: NewWhatsMessageBatch = {
  tipo: WhatsAppType['LIST_MATCH_REPROVED'],
  waidTo: 'copy Networked incremental',
  perfilID: 28007,
  status: WhatsStatus['OPEN'],
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
