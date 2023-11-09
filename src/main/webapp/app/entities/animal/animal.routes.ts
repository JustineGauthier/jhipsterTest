import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { AnimalComponent } from './list/animal.component';
import { AnimalDetailComponent } from './detail/animal-detail.component';
import { AnimalUpdateComponent } from './update/animal-update.component';
import AnimalResolve from './route/animal-routing-resolve.service';

const animalRoute: Routes = [
  {
    path: '',
    component: AnimalComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AnimalDetailComponent,
    resolve: {
      animal: AnimalResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AnimalUpdateComponent,
    resolve: {
      animal: AnimalResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AnimalUpdateComponent,
    resolve: {
      animal: AnimalResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default animalRoute;
