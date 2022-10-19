export interface ICategoriaVeiculo {
  id: number;
  nome?: string | null;
}

export type NewCategoriaVeiculo = Omit<ICategoriaVeiculo, 'id'> & { id: null };
