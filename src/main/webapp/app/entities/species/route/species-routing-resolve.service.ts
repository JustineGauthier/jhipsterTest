import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISpecies } from '../species.model';
import { SpeciesService } from '../service/species.service';

export const speciesResolve = (route: ActivatedRouteSnapshot): Observable<null | ISpecies> => {
  const id = route.params['id'];
  if (id) {
    return inject(SpeciesService)
      .find(id)
      .pipe(
        mergeMap((species: HttpResponse<ISpecies>) => {
          if (species.body) {
            return of(species.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default speciesResolve;
