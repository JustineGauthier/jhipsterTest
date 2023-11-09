import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IFood, NewFood } from '../food.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFood for edit and NewFoodFormGroupInput for create.
 */
type FoodFormGroupInput = IFood | PartialWithRequiredKeyOf<NewFood>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IFood | NewFood> = Omit<T, 'peremptionDate'> & {
  peremptionDate?: string | null;
};

type FoodFormRawValue = FormValueOf<IFood>;

type NewFoodFormRawValue = FormValueOf<NewFood>;

type FoodFormDefaults = Pick<NewFood, 'id' | 'peremptionDate' | 'animals' | 'typeOfHabitats'>;

type FoodFormGroupContent = {
  id: FormControl<FoodFormRawValue['id'] | NewFood['id']>;
  name: FormControl<FoodFormRawValue['name']>;
  color: FormControl<FoodFormRawValue['color']>;
  peremptionDate: FormControl<FoodFormRawValue['peremptionDate']>;
  animals: FormControl<FoodFormRawValue['animals']>;
  typeOfHabitats: FormControl<FoodFormRawValue['typeOfHabitats']>;
};

export type FoodFormGroup = FormGroup<FoodFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class FoodFormService {
  createFoodFormGroup(food: FoodFormGroupInput = { id: null }): FoodFormGroup {
    const foodRawValue = this.convertFoodToFoodRawValue({
      ...this.getFormDefaults(),
      ...food,
    });
    return new FormGroup<FoodFormGroupContent>({
      id: new FormControl(
        { value: foodRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(foodRawValue.name),
      color: new FormControl(foodRawValue.color),
      peremptionDate: new FormControl(foodRawValue.peremptionDate),
      animals: new FormControl(foodRawValue.animals ?? []),
      typeOfHabitats: new FormControl(foodRawValue.typeOfHabitats ?? []),
    });
  }

  getFood(form: FoodFormGroup): IFood | NewFood {
    return this.convertFoodRawValueToFood(form.getRawValue() as FoodFormRawValue | NewFoodFormRawValue);
  }

  resetForm(form: FoodFormGroup, food: FoodFormGroupInput): void {
    const foodRawValue = this.convertFoodToFoodRawValue({ ...this.getFormDefaults(), ...food });
    form.reset(
      {
        ...foodRawValue,
        id: { value: foodRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): FoodFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      peremptionDate: currentTime,
      animals: [],
      typeOfHabitats: [],
    };
  }

  private convertFoodRawValueToFood(rawFood: FoodFormRawValue | NewFoodFormRawValue): IFood | NewFood {
    return {
      ...rawFood,
      peremptionDate: dayjs(rawFood.peremptionDate, DATE_TIME_FORMAT),
    };
  }

  private convertFoodToFoodRawValue(
    food: IFood | (Partial<NewFood> & FoodFormDefaults),
  ): FoodFormRawValue | PartialWithRequiredKeyOf<NewFoodFormRawValue> {
    return {
      ...food,
      peremptionDate: food.peremptionDate ? food.peremptionDate.format(DATE_TIME_FORMAT) : undefined,
      animals: food.animals ?? [],
      typeOfHabitats: food.typeOfHabitats ?? [],
    };
  }
}
