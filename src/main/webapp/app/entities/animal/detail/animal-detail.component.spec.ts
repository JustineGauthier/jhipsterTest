import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { AnimalDetailComponent } from './animal-detail.component';

describe('Animal Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AnimalDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: AnimalDetailComponent,
              resolve: { animal: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(AnimalDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load animal on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', AnimalDetailComponent);

      // THEN
      expect(instance.animal).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
