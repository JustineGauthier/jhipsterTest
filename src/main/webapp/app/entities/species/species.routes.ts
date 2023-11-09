import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { SpeciesComponent } from './list/species.component';
import { SpeciesDetailComponent } from './detail/species-detail.component';
import { SpeciesUpdateComponent } from './update/species-update.component';
import SpeciesResolve from './route/species-routing-resolve.service';

const speciesRoute: Routes = [
  {
    path: '',
    component: SpeciesComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SpeciesDetailComponent,
    resolve: {
      species: SpeciesResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SpeciesUpdateComponent,
    resolve: {
      species: SpeciesResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SpeciesUpdateComponent,
    resolve: {
      species: SpeciesResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default speciesRoute;
