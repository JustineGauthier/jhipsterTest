import dayjs from 'dayjs/esm';
import { IAnimal } from 'app/entities/animal/animal.model';
import { ITypeOfHabitat } from 'app/entities/type-of-habitat/type-of-habitat.model';

export interface IFood {
  id: number;
  name?: string | null;
  color?: string | null;
  peremptionDate?: dayjs.Dayjs | null;
  animals?: Pick<IAnimal, 'id'>[] | null;
  typeOfHabitats?: Pick<ITypeOfHabitat, 'id'>[] | null;
}

export type NewFood = Omit<IFood, 'id'> & { id: null };
