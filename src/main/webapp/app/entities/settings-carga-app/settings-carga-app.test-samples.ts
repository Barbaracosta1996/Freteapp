import { ISettingsCargaApp, NewSettingsCargaApp } from './settings-carga-app.model';

export const sampleWithRequiredData: ISettingsCargaApp = {
  id: 29273,
};

export const sampleWithPartialData: ISettingsCargaApp = {
  id: 32371,
  rdCode: 'drive Pizza connect',
};

export const sampleWithFullData: ISettingsCargaApp = {
  id: 25874,
  rdCode: 'Alameda Congelado Dynamic',
  waToken: 'Sapatos digital application',
};

export const sampleWithNewData: NewSettingsCargaApp = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
