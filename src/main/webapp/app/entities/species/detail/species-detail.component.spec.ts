import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { SpeciesDetailComponent } from './species-detail.component';

describe('Species Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SpeciesDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: SpeciesDetailComponent,
              resolve: { species: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(SpeciesDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load species on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', SpeciesDetailComponent);

      // THEN
      expect(instance.species).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
