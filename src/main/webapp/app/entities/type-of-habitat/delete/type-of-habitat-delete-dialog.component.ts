import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ITypeOfHabitat } from '../type-of-habitat.model';
import { TypeOfHabitatService } from '../service/type-of-habitat.service';

@Component({
  standalone: true,
  templateUrl: './type-of-habitat-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class TypeOfHabitatDeleteDialogComponent {
  typeOfHabitat?: ITypeOfHabitat;

  constructor(
    protected typeOfHabitatService: TypeOfHabitatService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.typeOfHabitatService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
