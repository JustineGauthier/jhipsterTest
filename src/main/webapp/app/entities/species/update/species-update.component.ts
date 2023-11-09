import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ISpecies } from '../species.model';
import { SpeciesService } from '../service/species.service';
import { SpeciesFormService, SpeciesFormGroup } from './species-form.service';

@Component({
  standalone: true,
  selector: 'jhi-species-update',
  templateUrl: './species-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class SpeciesUpdateComponent implements OnInit {
  isSaving = false;
  species: ISpecies | null = null;

  editForm: SpeciesFormGroup = this.speciesFormService.createSpeciesFormGroup();

  constructor(
    protected speciesService: SpeciesService,
    protected speciesFormService: SpeciesFormService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ species }) => {
      this.species = species;
      if (species) {
        this.updateForm(species);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const species = this.speciesFormService.getSpecies(this.editForm);
    if (species.id !== null) {
      this.subscribeToSaveResponse(this.speciesService.update(species));
    } else {
      this.subscribeToSaveResponse(this.speciesService.create(species));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISpecies>>): void {
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

  protected updateForm(species: ISpecies): void {
    this.species = species;
    this.speciesFormService.resetForm(this.editForm, species);
  }
}
