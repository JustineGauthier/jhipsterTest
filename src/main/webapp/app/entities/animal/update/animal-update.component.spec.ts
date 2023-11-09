import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ISpecies } from 'app/entities/species/species.model';
import { SpeciesService } from 'app/entities/species/service/species.service';
import { AnimalService } from '../service/animal.service';
import { IAnimal } from '../animal.model';
import { AnimalFormService } from './animal-form.service';

import { AnimalUpdateComponent } from './animal-update.component';

describe('Animal Management Update Component', () => {
  let comp: AnimalUpdateComponent;
  let fixture: ComponentFixture<AnimalUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let animalFormService: AnimalFormService;
  let animalService: AnimalService;
  let speciesService: SpeciesService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), AnimalUpdateComponent],
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
      .overrideTemplate(AnimalUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AnimalUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    animalFormService = TestBed.inject(AnimalFormService);
    animalService = TestBed.inject(AnimalService);
    speciesService = TestBed.inject(SpeciesService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Species query and add missing value', () => {
      const animal: IAnimal = { id: 456 };
      const species: ISpecies = { id: 19353 };
      animal.species = species;

      const speciesCollection: ISpecies[] = [{ id: 10971 }];
      jest.spyOn(speciesService, 'query').mockReturnValue(of(new HttpResponse({ body: speciesCollection })));
      const additionalSpecies = [species];
      const expectedCollection: ISpecies[] = [...additionalSpecies, ...speciesCollection];
      jest.spyOn(speciesService, 'addSpeciesToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ animal });
      comp.ngOnInit();

      expect(speciesService.query).toHaveBeenCalled();
      expect(speciesService.addSpeciesToCollectionIfMissing).toHaveBeenCalledWith(
        speciesCollection,
        ...additionalSpecies.map(expect.objectContaining),
      );
      expect(comp.speciesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const animal: IAnimal = { id: 456 };
      const species: ISpecies = { id: 14610 };
      animal.species = species;

      activatedRoute.data = of({ animal });
      comp.ngOnInit();

      expect(comp.speciesSharedCollection).toContain(species);
      expect(comp.animal).toEqual(animal);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAnimal>>();
      const animal = { id: 123 };
      jest.spyOn(animalFormService, 'getAnimal').mockReturnValue(animal);
      jest.spyOn(animalService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ animal });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: animal }));
      saveSubject.complete();

      // THEN
      expect(animalFormService.getAnimal).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(animalService.update).toHaveBeenCalledWith(expect.objectContaining(animal));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAnimal>>();
      const animal = { id: 123 };
      jest.spyOn(animalFormService, 'getAnimal').mockReturnValue({ id: null });
      jest.spyOn(animalService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ animal: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: animal }));
      saveSubject.complete();

      // THEN
      expect(animalFormService.getAnimal).toHaveBeenCalled();
      expect(animalService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAnimal>>();
      const animal = { id: 123 };
      jest.spyOn(animalService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ animal });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(animalService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareSpecies', () => {
      it('Should forward to speciesService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(speciesService, 'compareSpecies');
        comp.compareSpecies(entity, entity2);
        expect(speciesService.compareSpecies).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
