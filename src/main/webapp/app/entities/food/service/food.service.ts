import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFood, NewFood } from '../food.model';

export type PartialUpdateFood = Partial<IFood> & Pick<IFood, 'id'>;

type RestOf<T extends IFood | NewFood> = Omit<T, 'peremptionDate'> & {
  peremptionDate?: string | null;
};

export type RestFood = RestOf<IFood>;

export type NewRestFood = RestOf<NewFood>;

export type PartialUpdateRestFood = RestOf<PartialUpdateFood>;

export type EntityResponseType = HttpResponse<IFood>;
export type EntityArrayResponseType = HttpResponse<IFood[]>;

@Injectable({ providedIn: 'root' })
export class FoodService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/foods');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(food: NewFood): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(food);
    return this.http.post<RestFood>(this.resourceUrl, copy, { observe: 'response' }).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(food: IFood): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(food);
    return this.http
      .put<RestFood>(`${this.resourceUrl}/${this.getFoodIdentifier(food)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(food: PartialUpdateFood): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(food);
    return this.http
      .patch<RestFood>(`${this.resourceUrl}/${this.getFoodIdentifier(food)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestFood>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestFood[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getFoodIdentifier(food: Pick<IFood, 'id'>): number {
    return food.id;
  }

  compareFood(o1: Pick<IFood, 'id'> | null, o2: Pick<IFood, 'id'> | null): boolean {
    return o1 && o2 ? this.getFoodIdentifier(o1) === this.getFoodIdentifier(o2) : o1 === o2;
  }

  addFoodToCollectionIfMissing<Type extends Pick<IFood, 'id'>>(
    foodCollection: Type[],
    ...foodsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const foods: Type[] = foodsToCheck.filter(isPresent);
    if (foods.length > 0) {
      const foodCollectionIdentifiers = foodCollection.map(foodItem => this.getFoodIdentifier(foodItem)!);
      const foodsToAdd = foods.filter(foodItem => {
        const foodIdentifier = this.getFoodIdentifier(foodItem);
        if (foodCollectionIdentifiers.includes(foodIdentifier)) {
          return false;
        }
        foodCollectionIdentifiers.push(foodIdentifier);
        return true;
      });
      return [...foodsToAdd, ...foodCollection];
    }
    return foodCollection;
  }

  protected convertDateFromClient<T extends IFood | NewFood | PartialUpdateFood>(food: T): RestOf<T> {
    return {
      ...food,
      peremptionDate: food.peremptionDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restFood: RestFood): IFood {
    return {
      ...restFood,
      peremptionDate: restFood.peremptionDate ? dayjs(restFood.peremptionDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestFood>): HttpResponse<IFood> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestFood[]>): HttpResponse<IFood[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
