import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IAnimal } from 'app/entities/animal/animal.model';
import { AnimalService } from 'app/entities/animal/service/animal.service';
import { ITypeOfHabitat } from 'app/entities/type-of-habitat/type-of-habitat.model';
import { TypeOfHabitatService } from 'app/entities/type-of-habitat/service/type-of-habitat.service';
import { IFood } from '../food.model';
import { FoodService } from '../service/food.service';
import { FoodFormService } from './food-form.service';

import { FoodUpdateComponent } from './food-update.component';

describe('Food Management Update Component', () => {
  let comp: FoodUpdateComponent;
  let fixture: ComponentFixture<FoodUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let foodFormService: FoodFormService;
  let foodService: FoodService;
  let animalService: AnimalService;
  let typeOfHabitatService: TypeOfHabitatService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), FoodUpdateComponent],
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
      .overrideTemplate(FoodUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FoodUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    foodFormService = TestBed.inject(FoodFormService);
    foodService = TestBed.inject(FoodService);
    animalService = TestBed.inject(AnimalService);
    typeOfHabitatService = TestBed.inject(TypeOfHabitatService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Animal query and add missing value', () => {
      const food: IFood = { id: 456 };
      const animals: IAnimal[] = [{ id: 7123 }];
      food.animals = animals;

      const animalCollection: IAnimal[] = [{ id: 10614 }];
      jest.spyOn(animalService, 'query').mockReturnValue(of(new HttpResponse({ body: animalCollection })));
      const additionalAnimals = [...animals];
      const expectedCollection: IAnimal[] = [...additionalAnimals, ...animalCollection];
      jest.spyOn(animalService, 'addAnimalToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ food });
      comp.ngOnInit();

      expect(animalService.query).toHaveBeenCalled();
      expect(animalService.addAnimalToCollectionIfMissing).toHaveBeenCalledWith(
        animalCollection,
        ...additionalAnimals.map(expect.objectContaining),
      );
      expect(comp.animalsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call TypeOfHabitat query and add missing value', () => {
      const food: IFood = { id: 456 };
      const typeOfHabitats: ITypeOfHabitat[] = [{ id: 13994 }];
      food.typeOfHabitats = typeOfHabitats;

      const typeOfHabitatCollection: ITypeOfHabitat[] = [{ id: 9709 }];
      jest.spyOn(typeOfHabitatService, 'query').mockReturnValue(of(new HttpResponse({ body: typeOfHabitatCollection })));
      const additionalTypeOfHabitats = [...typeOfHabitats];
      const expectedCollection: ITypeOfHabitat[] = [...additionalTypeOfHabitats, ...typeOfHabitatCollection];
      jest.spyOn(typeOfHabitatService, 'addTypeOfHabitatToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ food });
      comp.ngOnInit();

      expect(typeOfHabitatService.query).toHaveBeenCalled();
      expect(typeOfHabitatService.addTypeOfHabitatToCollectionIfMissing).toHaveBeenCalledWith(
        typeOfHabitatCollection,
        ...additionalTypeOfHabitats.map(expect.objectContaining),
      );
      expect(comp.typeOfHabitatsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const food: IFood = { id: 456 };
      const animal: IAnimal = { id: 13354 };
      food.animals = [animal];
      const typeOfHabitat: ITypeOfHabitat = { id: 30046 };
      food.typeOfHabitats = [typeOfHabitat];

      activatedRoute.data = of({ food });
      comp.ngOnInit();

      expect(comp.animalsSharedCollection).toContain(animal);
      expect(comp.typeOfHabitatsSharedCollection).toContain(typeOfHabitat);
      expect(comp.food).toEqual(food);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFood>>();
      const food = { id: 123 };
      jest.spyOn(foodFormService, 'getFood').mockReturnValue(food);
      jest.spyOn(foodService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ food });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: food }));
      saveSubject.complete();

      // THEN
      expect(foodFormService.getFood).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(foodService.update).toHaveBeenCalledWith(expect.objectContaining(food));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFood>>();
      const food = { id: 123 };
      jest.spyOn(foodFormService, 'getFood').mockReturnValue({ id: null });
      jest.spyOn(foodService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ food: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: food }));
      saveSubject.complete();

      // THEN
      expect(foodFormService.getFood).toHaveBeenCalled();
      expect(foodService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFood>>();
      const food = { id: 123 };
      jest.spyOn(foodService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ food });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(foodService.update).toHaveBeenCalled();
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

    describe('compareTypeOfHabitat', () => {
      it('Should forward to typeOfHabitatService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(typeOfHabitatService, 'compareTypeOfHabitat');
        comp.compareTypeOfHabitat(entity, entity2);
        expect(typeOfHabitatService.compareTypeOfHabitat).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
