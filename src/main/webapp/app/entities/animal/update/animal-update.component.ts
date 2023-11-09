import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ISpecies } from 'app/entities/species/species.model';
import { SpeciesService } from 'app/entities/species/service/species.service';
import { IAnimal } from '../animal.model';
import { AnimalService } from '../service/animal.service';
import { AnimalFormService, AnimalFormGroup } from './animal-form.service';

@Component({
  standalone: true,
  selector: 'jhi-animal-update',
  templateUrl: './animal-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class AnimalUpdateComponent implements OnInit {
  isSaving = false;
  animal: IAnimal | null = null;

  speciesSharedCollection: ISpecies[] = [];

  editForm: AnimalFormGroup = this.animalFormService.createAnimalFormGroup();

  constructor(
    protected animalService: AnimalService,
    protected animalFormService: AnimalFormService,
    protected speciesService: SpeciesService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareSpecies = (o1: ISpecies | null, o2: ISpecies | null): boolean => this.speciesService.compareSpecies(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ animal }) => {
      this.animal = animal;
      if (animal) {
        this.updateForm(animal);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const animal = this.animalFormService.getAnimal(this.editForm);
    if (animal.id !== null) {
      this.subscribeToSaveResponse(this.animalService.update(animal));
    } else {
      this.subscribeToSaveResponse(this.animalService.create(animal));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAnimal>>): void {
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

  protected updateForm(animal: IAnimal): void {
    this.animal = animal;
    this.animalFormService.resetForm(this.editForm, animal);

    this.speciesSharedCollection = this.speciesService.addSpeciesToCollectionIfMissing<ISpecies>(
      this.speciesSharedCollection,
      animal.species,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.speciesService
      .query()
      .pipe(map((res: HttpResponse<ISpecies[]>) => res.body ?? []))
      .pipe(map((species: ISpecies[]) => this.speciesService.addSpeciesToCollectionIfMissing<ISpecies>(species, this.animal?.species)))
      .subscribe((species: ISpecies[]) => (this.speciesSharedCollection = species));
  }
}
