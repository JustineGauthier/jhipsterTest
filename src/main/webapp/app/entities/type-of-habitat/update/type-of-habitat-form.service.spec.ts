import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../type-of-habitat.test-samples';

import { TypeOfHabitatFormService } from './type-of-habitat-form.service';

describe('TypeOfHabitat Form Service', () => {
  let service: TypeOfHabitatFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TypeOfHabitatFormService);
  });

  describe('Service methods', () => {
    describe('createTypeOfHabitatFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTypeOfHabitatFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            categorie: expect.any(Object),
            location: expect.any(Object),
            ground: expect.any(Object),
            animals: expect.any(Object),
          }),
        );
      });

      it('passing ITypeOfHabitat should create a new form with FormGroup', () => {
        const formGroup = service.createTypeOfHabitatFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            categorie: expect.any(Object),
            location: expect.any(Object),
            ground: expect.any(Object),
            animals: expect.any(Object),
          }),
        );
      });
    });

    describe('getTypeOfHabitat', () => {
      it('should return NewTypeOfHabitat for default TypeOfHabitat initial value', () => {
        const formGroup = service.createTypeOfHabitatFormGroup(sampleWithNewData);

        const typeOfHabitat = service.getTypeOfHabitat(formGroup) as any;

        expect(typeOfHabitat).toMatchObject(sampleWithNewData);
      });

      it('should return NewTypeOfHabitat for empty TypeOfHabitat initial value', () => {
        const formGroup = service.createTypeOfHabitatFormGroup();

        const typeOfHabitat = service.getTypeOfHabitat(formGroup) as any;

        expect(typeOfHabitat).toMatchObject({});
      });

      it('should return ITypeOfHabitat', () => {
        const formGroup = service.createTypeOfHabitatFormGroup(sampleWithRequiredData);

        const typeOfHabitat = service.getTypeOfHabitat(formGroup) as any;

        expect(typeOfHabitat).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITypeOfHabitat should not enable id FormControl', () => {
        const formGroup = service.createTypeOfHabitatFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTypeOfHabitat should disable id FormControl', () => {
        const formGroup = service.createTypeOfHabitatFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
