import { TipoCategoria } from 'app/entities/enumerations/tipo-categoria.model';

import { IFrota, NewFrota } from './frota.model';

export const sampleWithRequiredData: IFrota = {
  id: 5747,
  nome: 'Profit-focused integrate',
  modelo: 'Credit digital Incrível',
  ano: 'tran',
  tipoCategoria: TipoCategoria['REBOQUE'],
};

export const sampleWithPartialData: IFrota = {
  id: 44938,
  nome: 'Investment extranet',
  modelo: 'Web Toalhas',
  marca: 'task-force Belize celeste',
  ano: 'Cord',
  tipoCategoria: TipoCategoria['GUINCHO'],
};

export const sampleWithFullData: IFrota = {
  id: 41129,
  nome: 'leading-edge',
  modelo: 'interface quantify Cross-group',
  marca: 'Granito attitude-oriented',
  ano: 'Fant',
  tipoCategoria: TipoCategoria['REBOQUE'],
  obsCategoriaOutro: 'e-services',
};

export const sampleWithNewData: NewFrota = {
  nome: 'radical marrom',
  modelo: 'transition',
  ano: 'back',
  tipoCategoria: TipoCategoria['REBOQUE'],
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
