import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IAnimal } from 'app/entities/animal/animal.model';
import { AnimalService } from 'app/entities/animal/service/animal.service';
import { ITypeOfHabitat } from 'app/entities/type-of-habitat/type-of-habitat.model';
import { TypeOfHabitatService } from 'app/entities/type-of-habitat/service/type-of-habitat.service';
import { FoodService } from '../service/food.service';
import { IFood } from '../food.model';
import { FoodFormService, FoodFormGroup } from './food-form.service';

@Component({
  standalone: true,
  selector: 'jhi-food-update',
  templateUrl: './food-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class FoodUpdateComponent implements OnInit {
  isSaving = false;
  food: IFood | null = null;

  animalsSharedCollection: IAnimal[] = [];
  typeOfHabitatsSharedCollection: ITypeOfHabitat[] = [];

  editForm: FoodFormGroup = this.foodFormService.createFoodFormGroup();

  constructor(
    protected foodService: FoodService,
    protected foodFormService: FoodFormService,
    protected animalService: AnimalService,
    protected typeOfHabitatService: TypeOfHabitatService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareAnimal = (o1: IAnimal | null, o2: IAnimal | null): boolean => this.animalService.compareAnimal(o1, o2);

  compareTypeOfHabitat = (o1: ITypeOfHabitat | null, o2: ITypeOfHabitat | null): boolean =>
    this.typeOfHabitatService.compareTypeOfHabitat(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ food }) => {
      this.food = food;
      if (food) {
        this.updateForm(food);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const food = this.foodFormService.getFood(this.editForm);
    if (food.id !== null) {
      this.subscribeToSaveResponse(this.foodService.update(food));
    } else {
      this.subscribeToSaveResponse(this.foodService.create(food));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFood>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(food: IFood): void {
    this.food = food;
    this.foodFormService.resetForm(this.editForm, food);

    this.animalsSharedCollection = this.animalService.addAnimalToCollectionIfMissing<IAnimal>(
      this.animalsSharedCollection,
      ...(food.animals ?? []),
    );
    this.typeOfHabitatsSharedCollection = this.typeOfHabitatService.addTypeOfHabitatToCollectionIfMissing<ITypeOfHabitat>(
      this.typeOfHabitatsSharedCollection,
      ...(food.typeOfHabitats ?? []),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.animalService
      .query()
      .pipe(map((res: HttpResponse<IAnimal[]>) => res.body ?? []))
      .pipe(map((animals: IAnimal[]) => this.animalService.addAnimalToCollectionIfMissing<IAnimal>(animals, ...(this.food?.animals ?? []))))
      .subscribe((animals: IAnimal[]) => (this.animalsSharedCollection = animals));

    this.typeOfHabitatService
      .query()
      .pipe(map((res: HttpResponse<ITypeOfHabitat[]>) => res.body ?? []))
      .pipe(
        map((typeOfHabitats: ITypeOfHabitat[]) =>
          this.typeOfHabitatService.addTypeOfHabitatToCollectionIfMissing<ITypeOfHabitat>(
            typeOfHabitats,
            ...(this.food?.typeOfHabitats ?? []),
          ),
        ),
      )
      .subscribe((typeOfHabitats: ITypeOfHabitat[]) => (this.typeOfHabitatsSharedCollection = typeOfHabitats));
  }
}
