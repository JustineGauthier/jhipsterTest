import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ITypeOfHabitat, NewTypeOfHabitat } from '../type-of-habitat.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITypeOfHabitat for edit and NewTypeOfHabitatFormGroupInput for create.
 */
type TypeOfHabitatFormGroupInput = ITypeOfHabitat | PartialWithRequiredKeyOf<NewTypeOfHabitat>;

type TypeOfHabitatFormDefaults = Pick<NewTypeOfHabitat, 'id' | 'animals' | 'foods'>;

type TypeOfHabitatFormGroupContent = {
  id: FormControl<ITypeOfHabitat['id'] | NewTypeOfHabitat['id']>;
  categorie: FormControl<ITypeOfHabitat['categorie']>;
  location: FormControl<ITypeOfHabitat['location']>;
  ground: FormControl<ITypeOfHabitat['ground']>;
  animals: FormControl<ITypeOfHabitat['animals']>;
  foods: FormControl<ITypeOfHabitat['foods']>;
};

export type TypeOfHabitatFormGroup = FormGroup<TypeOfHabitatFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TypeOfHabitatFormService {
  createTypeOfHabitatFormGroup(typeOfHabitat: TypeOfHabitatFormGroupInput = { id: null }): TypeOfHabitatFormGroup {
    const typeOfHabitatRawValue = {
      ...this.getFormDefaults(),
      ...typeOfHabitat,
    };
    return new FormGroup<TypeOfHabitatFormGroupContent>({
      id: new FormControl(
        { value: typeOfHabitatRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      categorie: new FormControl(typeOfHabitatRawValue.categorie),
      location: new FormControl(typeOfHabitatRawValue.location),
      ground: new FormControl(typeOfHabitatRawValue.ground),
      animals: new FormControl(typeOfHabitatRawValue.animals ?? []),
      foods: new FormControl(typeOfHabitatRawValue.foods ?? []),
    });
  }

  getTypeOfHabitat(form: TypeOfHabitatFormGroup): ITypeOfHabitat | NewTypeOfHabitat {
    return form.getRawValue() as ITypeOfHabitat | NewTypeOfHabitat;
  }

  resetForm(form: TypeOfHabitatFormGroup, typeOfHabitat: TypeOfHabitatFormGroupInput): void {
    const typeOfHabitatRawValue = { ...this.getFormDefaults(), ...typeOfHabitat };
    form.reset(
      {
        ...typeOfHabitatRawValue,
        id: { value: typeOfHabitatRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): TypeOfHabitatFormDefaults {
    return {
      id: null,
      animals: [],
      foods: [],
    };
  }
}
