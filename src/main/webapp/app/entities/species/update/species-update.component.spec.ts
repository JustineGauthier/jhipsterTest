import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { SpeciesService } from '../service/species.service';
import { ISpecies } from '../species.model';
import { SpeciesFormService } from './species-form.service';

import { SpeciesUpdateComponent } from './species-update.component';

describe('Species Management Update Component', () => {
  let comp: SpeciesUpdateComponent;
  let fixture: ComponentFixture<SpeciesUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let speciesFormService: SpeciesFormService;
  let speciesService: SpeciesService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), SpeciesUpdateComponent],
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
      .overrideTemplate(SpeciesUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SpeciesUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    speciesFormService = TestBed.inject(SpeciesFormService);
    speciesService = TestBed.inject(SpeciesService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const species: ISpecies = { id: 456 };

      activatedRoute.data = of({ species });
      comp.ngOnInit();

      expect(comp.species).toEqual(species);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISpecies>>();
      const species = { id: 123 };
      jest.spyOn(speciesFormService, 'getSpecies').mockReturnValue(species);
      jest.spyOn(speciesService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ species });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: species }));
      saveSubject.complete();

      // THEN
      expect(speciesFormService.getSpecies).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(speciesService.update).toHaveBeenCalledWith(expect.objectContaining(species));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISpecies>>();
      const species = { id: 123 };
      jest.spyOn(speciesFormService, 'getSpecies').mockReturnValue({ id: null });
      jest.spyOn(speciesService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ species: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: species }));
      saveSubject.complete();

      // THEN
      expect(speciesFormService.getSpecies).toHaveBeenCalled();
      expect(speciesService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISpecies>>();
      const species = { id: 123 };
      jest.spyOn(speciesService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ species });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(speciesService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
