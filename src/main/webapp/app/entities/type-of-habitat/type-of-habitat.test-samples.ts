import { ITypeOfHabitat, NewTypeOfHabitat } from './type-of-habitat.model';

export const sampleWithRequiredData: ITypeOfHabitat = {
  id: 2944,
};

export const sampleWithPartialData: ITypeOfHabitat = {
  id: 2284,
  categorie: 'puisque mourir',
  location: 'devant',
  ground: 'quand ? admirablement',
};

export const sampleWithFullData: ITypeOfHabitat = {
  id: 9354,
  categorie: "louer à l'encontre de fonctionnaire",
  location: 'entre-temps ci',
  ground: 'à moins de multiple dissimuler',
};

export const sampleWithNewData: NewTypeOfHabitat = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
