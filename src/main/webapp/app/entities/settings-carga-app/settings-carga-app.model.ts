export interface ISettingsCargaApp {
  id: number;
  rdCode?: string | null;
  waToken?: string | null;
}

export type NewSettingsCargaApp = Omit<ISettingsCargaApp, 'id'> & { id: null };
