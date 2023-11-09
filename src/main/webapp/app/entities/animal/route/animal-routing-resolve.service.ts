import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAnimal } from '../animal.model';
import { AnimalService } from '../service/animal.service';

export const animalResolve = (route: ActivatedRouteSnapshot): Observable<null | IAnimal> => {
  const id = route.params['id'];
  if (id) {
    return inject(AnimalService)
      .find(id)
      .pipe(
        mergeMap((animal: HttpResponse<IAnimal>) => {
          if (animal.body) {
            return of(animal.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default animalResolve;
