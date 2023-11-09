import { ITypeOfHabitat, NewTypeOfHabitat } from './type-of-habitat.model';

export const sampleWithRequiredData: ITypeOfHabitat = {
  id: 13994,
};

export const sampleWithPartialData: ITypeOfHabitat = {
  id: 4195,
  categorie: 'chef',
  ground: 'dans assumer',
};

export const sampleWithFullData: ITypeOfHabitat = {
  id: 13356,
  categorie: 'boum',
  location: 'ferme',
  ground: 'areu areu',
};

export const sampleWithNewData: NewTypeOfHabitat = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
