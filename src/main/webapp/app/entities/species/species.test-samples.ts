import { ISpecies, NewSpecies } from './species.model';

export const sampleWithRequiredData: ISpecies = {
  id: 3470,
};

export const sampleWithPartialData: ISpecies = {
  id: 16472,
  size: 26187,
};

export const sampleWithFullData: ISpecies = {
  id: 24499,
  name: 'aussitôt que',
  size: 21617,
};

export const sampleWithNewData: NewSpecies = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
