import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITypeOfHabitat } from '../type-of-habitat.model';
import { TypeOfHabitatService } from '../service/type-of-habitat.service';

export const typeOfHabitatResolve = (route: ActivatedRouteSnapshot): Observable<null | ITypeOfHabitat> => {
  const id = route.params['id'];
  if (id) {
    return inject(TypeOfHabitatService)
      .find(id)
      .pipe(
        mergeMap((typeOfHabitat: HttpResponse<ITypeOfHabitat>) => {
          if (typeOfHabitat.body) {
            return of(typeOfHabitat.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default typeOfHabitatResolve;
