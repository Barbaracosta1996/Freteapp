import { ISettingsContracts, NewSettingsContracts } from './settings-contracts.model';

export const sampleWithRequiredData: ISettingsContracts = {
  id: 45114,
};

export const sampleWithPartialData: ISettingsContracts = {
  id: 45137,
  terms: '../fake-data/blob/hipster.png',
  termsContentType: 'unknown',
  contractDefault: '../fake-data/blob/hipster.png',
  contractDefaultContentType: 'unknown',
  privacy: '../fake-data/blob/hipster.png',
  privacyContentType: 'unknown',
};

export const sampleWithFullData: ISettingsContracts = {
  id: 62117,
  terms: '../fake-data/blob/hipster.png',
  termsContentType: 'unknown',
  contractDefault: '../fake-data/blob/hipster.png',
  contractDefaultContentType: 'unknown',
  privacy: '../fake-data/blob/hipster.png',
  privacyContentType: 'unknown',
};

export const sampleWithNewData: NewSettingsContracts = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
