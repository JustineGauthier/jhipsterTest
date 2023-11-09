import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { TypeOfHabitatService } from '../service/type-of-habitat.service';

import { TypeOfHabitatComponent } from './type-of-habitat.component';

describe('TypeOfHabitat Management Component', () => {
  let comp: TypeOfHabitatComponent;
  let fixture: ComponentFixture<TypeOfHabitatComponent>;
  let service: TypeOfHabitatService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([{ path: 'type-of-habitat', component: TypeOfHabitatComponent }]),
        HttpClientTestingModule,
        TypeOfHabitatComponent,
      ],
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
      .overrideTemplate(TypeOfHabitatComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TypeOfHabitatComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(TypeOfHabitatService);

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
    expect(comp.typeOfHabitats?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to typeOfHabitatService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getTypeOfHabitatIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getTypeOfHabitatIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
