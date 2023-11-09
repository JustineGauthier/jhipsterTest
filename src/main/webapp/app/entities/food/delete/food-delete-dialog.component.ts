import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IFood } from '../food.model';
import { FoodService } from '../service/food.service';

@Component({
  standalone: true,
  templateUrl: './food-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class FoodDeleteDialogComponent {
  food?: IFood;

  constructor(
    protected foodService: FoodService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.foodService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
