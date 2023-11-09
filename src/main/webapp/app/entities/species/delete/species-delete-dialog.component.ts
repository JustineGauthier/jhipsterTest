import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ISpecies } from '../species.model';
import { SpeciesService } from '../service/species.service';

@Component({
  standalone: true,
  templateUrl: './species-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class SpeciesDeleteDialogComponent {
  species?: ISpecies;

  constructor(
    protected speciesService: SpeciesService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.speciesService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
