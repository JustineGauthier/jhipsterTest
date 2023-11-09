import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IAnimal } from 'app/entities/animal/animal.model';
import { AnimalService } from 'app/entities/animal/service/animal.service';
import { TypeOfHabitatService } from '../service/type-of-habitat.service';
import { ITypeOfHabitat } from '../type-of-habitat.model';
import { TypeOfHabitatFormService } from './type-of-habitat-form.service';

import { TypeOfHabitatUpdateComponent } from './type-of-habitat-update.component';

describe('TypeOfHabitat Management Update Component', () => {
  let comp: TypeOfHabitatUpdateComponent;
  let fixture: ComponentFixture<TypeOfHabitatUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let typeOfHabitatFormService: TypeOfHabitatFormService;
  let typeOfHabitatService: TypeOfHabitatService;
  let animalService: AnimalService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), TypeOfHabitatUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(TypeOfHabitatUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TypeOfHabitatUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    typeOfHabitatFormService = TestBed.inject(TypeOfHabitatFormService);
    typeOfHabitatService = TestBed.inject(TypeOfHabitatService);
    animalService = TestBed.inject(AnimalService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Animal query and add missing value', () => {
      const typeOfHabitat: ITypeOfHabitat = { id: 456 };
      const animals: IAnimal[] = [{ id: 22284 }];
      typeOfHabitat.animals = animals;

      const animalCollection: IAnimal[] = [{ id: 17307 }];
      jest.spyOn(animalService, 'query').mockReturnValue(of(new HttpResponse({ body: animalCollection })));
      const additionalAnimals = [...animals];
      const expectedCollection: IAnimal[] = [...additionalAnimals, ...animalCollection];
      jest.spyOn(animalService, 'addAnimalToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ typeOfHabitat });
      comp.ngOnInit();

      expect(animalService.query).toHaveBeenCalled();
      expect(animalService.addAnimalToCollectionIfMissing).toHaveBeenCalledWith(
        animalCollection,
        ...additionalAnimals.map(expect.objectContaining),
      );
      expect(comp.animalsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const typeOfHabitat: ITypeOfHabitat = { id: 456 };
      const animal: IAnimal = { id: 18622 };
      typeOfHabitat.animals = [animal];

      activatedRoute.data = of({ typeOfHabitat });
      comp.ngOnInit();

      expect(comp.animalsSharedCollection).toContain(animal);
      expect(comp.typeOfHabitat).toEqual(typeOfHabitat);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITypeOfHabitat>>();
      const typeOfHabitat = { id: 123 };
      jest.spyOn(typeOfHabitatFormService, 'getTypeOfHabitat').mockReturnValue(typeOfHabitat);
      jest.spyOn(typeOfHabitatService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ typeOfHabitat });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: typeOfHabitat }));
      saveSubject.complete();

      // THEN
      expect(typeOfHabitatFormService.getTypeOfHabitat).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(typeOfHabitatService.update).toHaveBeenCalledWith(expect.objectContaining(typeOfHabitat));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITypeOfHabitat>>();
      const typeOfHabitat = { id: 123 };
      jest.spyOn(typeOfHabitatFormService, 'getTypeOfHabitat').mockReturnValue({ id: null });
      jest.spyOn(typeOfHabitatService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ typeOfHabitat: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: typeOfHabitat }));
      saveSubject.complete();

      // THEN
      expect(typeOfHabitatFormService.getTypeOfHabitat).toHaveBeenCalled();
      expect(typeOfHabitatService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITypeOfHabitat>>();
      const typeOfHabitat = { id: 123 };
      jest.spyOn(typeOfHabitatService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ typeOfHabitat });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(typeOfHabitatService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareAnimal', () => {
      it('Should forward to animalService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(animalService, 'compareAnimal');
        comp.compareAnimal(entity, entity2);
        expect(animalService.compareAnimal).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
