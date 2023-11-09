import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'animal',
        data: { pageTitle: 'jhipsterTestApp.animal.home.title' },
        loadChildren: () => import('./animal/animal.routes'),
      },
      {
        path: 'species',
        data: { pageTitle: 'jhipsterTestApp.species.home.title' },
        loadChildren: () => import('./species/species.routes'),
      },
      {
        path: 'type-of-habitat',
        data: { pageTitle: 'jhipsterTestApp.typeOfHabitat.home.title' },
        loadChildren: () => import('./type-of-habitat/type-of-habitat.routes'),
      },
      {
        path: 'food',
        data: { pageTitle: 'jhipsterTestApp.food.home.title' },
        loadChildren: () => import('./food/food.routes'),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
