import { Component, Input } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { ITypeOfHabitat } from '../type-of-habitat.model';

@Component({
  standalone: true,
  selector: 'jhi-type-of-habitat-detail',
  templateUrl: './type-of-habitat-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class TypeOfHabitatDetailComponent {
  @Input() typeOfHabitat: ITypeOfHabitat | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  previousState(): void {
    window.history.back();
  }
}
