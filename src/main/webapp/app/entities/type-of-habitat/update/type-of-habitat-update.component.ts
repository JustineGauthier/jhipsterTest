import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IAnimal } from 'app/entities/animal/animal.model';
import { AnimalService } from 'app/entities/animal/service/animal.service';
import { ITypeOfHabitat } from '../type-of-habitat.model';
import { TypeOfHabitatService } from '../service/type-of-habitat.service';
import { TypeOfHabitatFormService, TypeOfHabitatFormGroup } from './type-of-habitat-form.service';

@Component({
  standalone: true,
  selector: 'jhi-type-of-habitat-update',
  templateUrl: './type-of-habitat-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class TypeOfHabitatUpdateComponent implements OnInit {
  isSaving = false;
  typeOfHabitat: ITypeOfHabitat | null = null;

  animalsSharedCollection: IAnimal[] = [];

  editForm: TypeOfHabitatFormGroup = this.typeOfHabitatFormService.createTypeOfHabitatFormGroup();

  constructor(
    protected typeOfHabitatService: TypeOfHabitatService,
    protected typeOfHabitatFormService: TypeOfHabitatFormService,
    protected animalService: AnimalService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareAnimal = (o1: IAnimal | null, o2: IAnimal | null): boolean => this.animalService.compareAnimal(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ typeOfHabitat }) => {
      this.typeOfHabitat = typeOfHabitat;
      if (typeOfHabitat) {
        this.updateForm(typeOfHabitat);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const typeOfHabitat = this.typeOfHabitatFormService.getTypeOfHabitat(this.editForm);
    if (typeOfHabitat.id !== null) {
      this.subscribeToSaveResponse(this.typeOfHabitatService.update(typeOfHabitat));
    } else {
      this.subscribeToSaveResponse(this.typeOfHabitatService.create(typeOfHabitat));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITypeOfHabitat>>): void {
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

  protected updateForm(typeOfHabitat: ITypeOfHabitat): void {
    this.typeOfHabitat = typeOfHabitat;
    this.typeOfHabitatFormService.resetForm(this.editForm, typeOfHabitat);

    this.animalsSharedCollection = this.animalService.addAnimalToCollectionIfMissing<IAnimal>(
      this.animalsSharedCollection,
      ...(typeOfHabitat.animals ?? []),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.animalService
      .query()
      .pipe(map((res: HttpResponse<IAnimal[]>) => res.body ?? []))
      .pipe(
        map((animals: IAnimal[]) =>
          this.animalService.addAnimalToCollectionIfMissing<IAnimal>(animals, ...(this.typeOfHabitat?.animals ?? [])),
        ),
      )
      .subscribe((animals: IAnimal[]) => (this.animalsSharedCollection = animals));
  }
}
