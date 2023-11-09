import { IAnimal, NewAnimal } from './animal.model';

export const sampleWithRequiredData: IAnimal = {
  id: 26389,
};

export const sampleWithPartialData: IAnimal = {
  id: 29526,
  name: 'pacifique tantôt',
  age: 17678,
  character: "à l'égard de accorder",
};

export const sampleWithFullData: IAnimal = {
  id: 24251,
  name: 'aigre',
  age: 30449,
  character: 'après que chez',
};

export const sampleWithNewData: NewAnimal = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
