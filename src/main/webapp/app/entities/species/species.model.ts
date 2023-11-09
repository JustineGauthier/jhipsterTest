export interface ISpecies {
  id: number;
  name?: string | null;
  size?: number | null;
}

export type NewSpecies = Omit<ISpecies, 'id'> & { id: null };
