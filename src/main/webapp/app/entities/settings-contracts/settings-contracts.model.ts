export interface ISettingsContracts {
  id: number;
  terms?: string | null;
  termsContentType?: string | null;
  contractDefault?: string | null;
  contractDefaultContentType?: string | null;
  privacy?: string | null;
  privacyContentType?: string | null;
}

export type NewSettingsContracts = Omit<ISettingsContracts, 'id'> & { id: null };
