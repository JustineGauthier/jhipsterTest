import { IAnimal } from 'app/entities/animal/animal.model';

export interface ITypeOfHabitat {
  id: number;
  categorie?: string | null;
  location?: string | null;
  ground?: string | null;
  animals?: Pick<IAnimal, 'id'>[] | null;
}

export type NewTypeOfHabitat = Omit<ITypeOfHabitat, 'id'> & { id: null };
