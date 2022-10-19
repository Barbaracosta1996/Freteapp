import { ICategoriaVeiculo, NewCategoriaVeiculo } from './categoria-veiculo.model';

export const sampleWithRequiredData: ICategoriaVeiculo = {
  id: 6270,
  nome: 'users whiteboard Home',
};

export const sampleWithPartialData: ICategoriaVeiculo = {
  id: 96416,
  nome: 'invoice Developer',
};

export const sampleWithFullData: ICategoriaVeiculo = {
  id: 15788,
  nome: 'Agent',
};

export const sampleWithNewData: NewCategoriaVeiculo = {
  nome: 'Cambridgeshire intangible Sapatos',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
