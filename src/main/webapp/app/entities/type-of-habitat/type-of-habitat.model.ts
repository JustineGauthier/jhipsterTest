import { IAnimal } from 'app/entities/animal/animal.model';
import { IFood } from 'app/entities/food/food.model';

export interface ITypeOfHabitat {
  id: number;
  categorie?: string | null;
  location?: string | null;
  ground?: string | null;
  animals?: Pick<IAnimal, 'id'>[] | null;
  foods?: Pick<IFood, 'id'>[] | null;
}

export type NewTypeOfHabitat = Omit<ITypeOfHabitat, 'id'> & { id: null };
