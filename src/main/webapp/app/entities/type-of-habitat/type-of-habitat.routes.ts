import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { TypeOfHabitatComponent } from './list/type-of-habitat.component';
import { TypeOfHabitatDetailComponent } from './detail/type-of-habitat-detail.component';
import { TypeOfHabitatUpdateComponent } from './update/type-of-habitat-update.component';
import TypeOfHabitatResolve from './route/type-of-habitat-routing-resolve.service';

const typeOfHabitatRoute: Routes = [
  {
    path: '',
    component: TypeOfHabitatComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TypeOfHabitatDetailComponent,
    resolve: {
      typeOfHabitat: TypeOfHabitatResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TypeOfHabitatUpdateComponent,
    resolve: {
      typeOfHabitat: TypeOfHabitatResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TypeOfHabitatUpdateComponent,
    resolve: {
      typeOfHabitat: TypeOfHabitatResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default typeOfHabitatRoute;
