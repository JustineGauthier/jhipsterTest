import dayjs from 'dayjs/esm';

import { IFood, NewFood } from './food.model';

export const sampleWithRequiredData: IFood = {
  id: 20722,
};

export const sampleWithPartialData: IFood = {
  id: 1663,
  name: 'réprimer absorber',
};

export const sampleWithFullData: IFood = {
  id: 15077,
  name: 'malade bè',
  color: 'anthracite',
  peremptionDate: dayjs('2023-11-08T13:09'),
};

export const sampleWithNewData: NewFood = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
