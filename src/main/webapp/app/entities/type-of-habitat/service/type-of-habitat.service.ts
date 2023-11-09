import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITypeOfHabitat, NewTypeOfHabitat } from '../type-of-habitat.model';

export type PartialUpdateTypeOfHabitat = Partial<ITypeOfHabitat> & Pick<ITypeOfHabitat, 'id'>;

export type EntityResponseType = HttpResponse<ITypeOfHabitat>;
export type EntityArrayResponseType = HttpResponse<ITypeOfHabitat[]>;

@Injectable({ providedIn: 'root' })
export class TypeOfHabitatService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/type-of-habitats');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(typeOfHabitat: NewTypeOfHabitat): Observable<EntityResponseType> {
    return this.http.post<ITypeOfHabitat>(this.resourceUrl, typeOfHabitat, { observe: 'response' });
  }

  update(typeOfHabitat: ITypeOfHabitat): Observable<EntityResponseType> {
    return this.http.put<ITypeOfHabitat>(`${this.resourceUrl}/${this.getTypeOfHabitatIdentifier(typeOfHabitat)}`, typeOfHabitat, {
      observe: 'response',
    });
  }

  partialUpdate(typeOfHabitat: PartialUpdateTypeOfHabitat): Observable<EntityResponseType> {
    return this.http.patch<ITypeOfHabitat>(`${this.resourceUrl}/${this.getTypeOfHabitatIdentifier(typeOfHabitat)}`, typeOfHabitat, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITypeOfHabitat>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITypeOfHabitat[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getTypeOfHabitatIdentifier(typeOfHabitat: Pick<ITypeOfHabitat, 'id'>): number {
    return typeOfHabitat.id;
  }

  compareTypeOfHabitat(o1: Pick<ITypeOfHabitat, 'id'> | null, o2: Pick<ITypeOfHabitat, 'id'> | null): boolean {
    return o1 && o2 ? this.getTypeOfHabitatIdentifier(o1) === this.getTypeOfHabitatIdentifier(o2) : o1 === o2;
  }

  addTypeOfHabitatToCollectionIfMissing<Type extends Pick<ITypeOfHabitat, 'id'>>(
    typeOfHabitatCollection: Type[],
    ...typeOfHabitatsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const typeOfHabitats: Type[] = typeOfHabitatsToCheck.filter(isPresent);
    if (typeOfHabitats.length > 0) {
      const typeOfHabitatCollectionIdentifiers = typeOfHabitatCollection.map(
        typeOfHabitatItem => this.getTypeOfHabitatIdentifier(typeOfHabitatItem)!,
      );
      const typeOfHabitatsToAdd = typeOfHabitats.filter(typeOfHabitatItem => {
        const typeOfHabitatIdentifier = this.getTypeOfHabitatIdentifier(typeOfHabitatItem);
        if (typeOfHabitatCollectionIdentifiers.includes(typeOfHabitatIdentifier)) {
          return false;
        }
        typeOfHabitatCollectionIdentifiers.push(typeOfHabitatIdentifier);
        return true;
      });
      return [...typeOfHabitatsToAdd, ...typeOfHabitatCollection];
    }
    return typeOfHabitatCollection;
  }
}
