import { IRotasOfertas, NewRotasOfertas } from './rotas-ofertas.model';

export const sampleWithRequiredData: IRotasOfertas = {
  id: 167,
  rotas: '../fake-data/blob/hipster.txt',
};

export const sampleWithPartialData: IRotasOfertas = {
  id: 35189,
  rotas: '../fake-data/blob/hipster.txt',
};

export const sampleWithFullData: IRotasOfertas = {
  id: 25403,
  rotas: '../fake-data/blob/hipster.txt',
};

export const sampleWithNewData: NewRotasOfertas = {
  rotas: '../fake-data/blob/hipster.txt',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
