import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IOfertas } from '../ofertas.model';
import { OfertasService } from '../service/ofertas.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './ofertas-delete-dialog.component.html',
})
export class OfertasDeleteDialogComponent {
  ofertas?: IOfertas;

  constructor(protected ofertasService: OfertasService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.ofertasService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
