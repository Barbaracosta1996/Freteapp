import dayjs from 'dayjs/esm';

import { TipoPerfilDocumento } from 'app/entities/enumerations/tipo-perfil-documento.model';

import { IPerfilAnexos, NewPerfilAnexos } from './perfil-anexos.model';

export const sampleWithRequiredData: IPerfilAnexos = {
  id: 3203,
  dataPostagem: dayjs('2022-08-29'),
  tipoDocumento: TipoPerfilDocumento['OUTROS'],
};

export const sampleWithPartialData: IPerfilAnexos = {
  id: 76457,
  dataPostagem: dayjs('2022-08-28'),
  tipoDocumento: TipoPerfilDocumento['CNH'],
  descricao: 'Account Communications',
  urlFile: '../fake-data/blob/hipster.png',
  urlFileContentType: 'unknown',
};

export const sampleWithFullData: IPerfilAnexos = {
  id: 1835,
  dataPostagem: dayjs('2022-08-28'),
  tipoDocumento: TipoPerfilDocumento['CNH'],
  descricao: 'withdrawal',
  urlFile: '../fake-data/blob/hipster.png',
  urlFileContentType: 'unknown',
};

export const sampleWithNewData: NewPerfilAnexos = {
  dataPostagem: dayjs('2022-08-29'),
  tipoDocumento: TipoPerfilDocumento['OUTROS'],
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
