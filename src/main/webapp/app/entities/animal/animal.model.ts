import { ISpecies } from 'app/entities/species/species.model';
import { ITypeOfHabitat } from 'app/entities/type-of-habitat/type-of-habitat.model';

export interface IAnimal {
  id: number;
  name?: string | null;
  age?: number | null;
  character?: string | null;
  species?: Pick<ISpecies, 'id'> | null;
  typeOfHabitats?: Pick<ITypeOfHabitat, 'id'>[] | null;
}

export type NewAnimal = Omit<IAnimal, 'id'> & { id: null };
