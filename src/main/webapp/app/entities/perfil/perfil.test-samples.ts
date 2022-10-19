import { TipoConta } from 'app/entities/enumerations/tipo-conta.model';

import { IPerfil, NewPerfil } from './perfil.model';

export const sampleWithRequiredData: IPerfil = {
  id: 84740,
  tipoConta: TipoConta['MOTORISTA'],
  nome: 'ADP',
};

export const sampleWithPartialData: IPerfil = {
  id: 56216,
  tipoConta: TipoConta['MOTORISTA'],
  cep: 'Somoni Mouse matrices',
  nome: 'COM Pre-emptive Cambridgeshire',
  razaosocial: 'Group open-source',
  telefoneComercial: 'verde-azulado',
};

export const sampleWithFullData: IPerfil = {
  id: 54466,
  tipoConta: TipoConta['TRANSPORTADORA'],
  cpf: 'EXE microchip',
  cnpj: 'SMS web-enabled e-enable',
  rua: 'Extended networks',
  numero: 'calculating Feito Borracha',
  bairro: 'Roupas productivity',
  cidade: 'Lempira Cambridgeshire',
  estado: 'Lanka Granito Rua',
  cep: 'Assurance help-desk infomediaries',
  pais: 'Agent',
  nome: 'Solutions',
  razaosocial: 'paradigms generating Madeira',
  telefoneComercial: 'convergence Frango',
};

export const sampleWithNewData: NewPerfil = {
  tipoConta: TipoConta['TRANSPORTADORA'],
  nome: 'override best-of-breed Human',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
