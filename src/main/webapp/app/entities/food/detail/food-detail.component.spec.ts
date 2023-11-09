import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { FoodDetailComponent } from './food-detail.component';

describe('Food Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FoodDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: FoodDetailComponent,
              resolve: { food: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(FoodDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load food on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', FoodDetailComponent);

      // THEN
      expect(instance.food).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
