import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IRotasOfertas } from '../rotas-ofertas.model';
import { RotasOfertasService } from '../service/rotas-ofertas.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './rotas-ofertas-delete-dialog.component.html',
})
export class RotasOfertasDeleteDialogComponent {
  rotasOfertas?: IRotasOfertas;

  constructor(protected rotasOfertasService: RotasOfertasService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.rotasOfertasService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
