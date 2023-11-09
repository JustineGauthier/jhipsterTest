import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { AnimalService } from '../service/animal.service';

import { AnimalComponent } from './animal.component';

describe('Animal Management Component', () => {
  let comp: AnimalComponent;
  let fixture: ComponentFixture<AnimalComponent>;
  let service: AnimalService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes([{ path: 'animal', component: AnimalComponent }]), HttpClientTestingModule, AnimalComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            data: of({
              defaultSort: 'id,asc',
            }),
            queryParamMap: of(
              jest.requireActual('@angular/router').convertToParamMap({
                page: '1',
                size: '1',
                sort: 'id,desc',
              }),
            ),
            snapshot: { queryParams: {} },
          },
        },
      ],
    })
      .overrideTemplate(AnimalComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AnimalComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(AnimalService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        }),
      ),
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.animals?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to animalService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getAnimalIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getAnimalIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
