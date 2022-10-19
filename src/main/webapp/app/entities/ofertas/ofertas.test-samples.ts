import dayjs from 'dayjs/esm';

import { TipoCarga } from 'app/entities/enumerations/tipo-carga.model';
import { StatusOferta } from 'app/entities/enumerations/status-oferta.model';
import { TipoOferta } from 'app/entities/enumerations/tipo-oferta.model';
import { TipoTransporteOferta } from 'app/entities/enumerations/tipo-transporte-oferta.model';

import { IOfertas, NewOfertas } from './ofertas.model';

export const sampleWithRequiredData: IOfertas = {
  id: 1233,
  dataPostagem: dayjs('2022-08-29T13:05'),
  quantidade: 9063,
  tipoCarga: TipoCarga['CARRO'],
  localizacaoOrigem: '../fake-data/blob/hipster.txt',
  localizacaoDestino: '../fake-data/blob/hipster.txt',
  status: StatusOferta['AGUARDANDO_PROPOSTA'],
  tipoOferta: TipoOferta['CARGA'],
  destino: 'Incrível Barbados Mercearia',
  origem: 'reintermediate',
};

export const sampleWithPartialData: IOfertas = {
  id: 1567,
  dataPostagem: dayjs('2022-08-28T19:46'),
  quantidade: 48713,
  tipoCarga: TipoCarga['CARRO'],
  localizacaoOrigem: '../fake-data/blob/hipster.txt',
  localizacaoDestino: '../fake-data/blob/hipster.txt',
  dataColeta: dayjs('2022-08-29'),
  dataEntrega: dayjs('2022-08-29T12:39'),
  dataModificacao: dayjs('2022-08-29T12:50'),
  status: StatusOferta['ATENDIDA_INFOCARGAS'],
  tipoOferta: TipoOferta['CARGA'],
  tipoTransporte: TipoTransporteOferta['CEGONHA'],
  destino: 'Georgia mobile',
  origem: 'bandwidth Teclado',
};

export const sampleWithFullData: IOfertas = {
  id: 7576,
  dataPostagem: dayjs('2022-08-29T14:05'),
  quantidade: 58599,
  tipoCarga: TipoCarga['CARRO'],
  localizacaoOrigem: '../fake-data/blob/hipster.txt',
  localizacaoDestino: '../fake-data/blob/hipster.txt',
  dataColeta: dayjs('2022-08-29'),
  dataEntrega: dayjs('2022-08-28T22:11'),
  dataModificacao: dayjs('2022-08-28T21:13'),
  dataFechamento: dayjs('2022-08-29T09:53'),
  status: StatusOferta['CANELADA'],
  tipoOferta: TipoOferta['VAGAS'],
  tipoTransporte: TipoTransporteOferta['RAMPA'],
  destino: 'wireless Mercearia salmão',
  origem: 'Lustroso content-based do',
};

export const sampleWithNewData: NewOfertas = {
  dataPostagem: dayjs('2022-08-29T01:24'),
  quantidade: 74450,
  tipoCarga: TipoCarga['CARRO'],
  localizacaoOrigem: '../fake-data/blob/hipster.txt',
  localizacaoDestino: '../fake-data/blob/hipster.txt',
  status: StatusOferta['ATENDIDA'],
  tipoOferta: TipoOferta['CARGA'],
  destino: 'uniform',
  origem: 'Fundamental Chapéu',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
