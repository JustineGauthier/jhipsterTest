import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { TypeOfHabitatDetailComponent } from './type-of-habitat-detail.component';

describe('TypeOfHabitat Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TypeOfHabitatDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: TypeOfHabitatDetailComponent,
              resolve: { typeOfHabitat: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(TypeOfHabitatDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load typeOfHabitat on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', TypeOfHabitatDetailComponent);

      // THEN
      expect(instance.typeOfHabitat).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
