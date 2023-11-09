import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { FoodComponent } from './list/food.component';
import { FoodDetailComponent } from './detail/food-detail.component';
import { FoodUpdateComponent } from './update/food-update.component';
import FoodResolve from './route/food-routing-resolve.service';

const foodRoute: Routes = [
  {
    path: '',
    component: FoodComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FoodDetailComponent,
    resolve: {
      food: FoodResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FoodUpdateComponent,
    resolve: {
      food: FoodResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FoodUpdateComponent,
    resolve: {
      food: FoodResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default foodRoute;
