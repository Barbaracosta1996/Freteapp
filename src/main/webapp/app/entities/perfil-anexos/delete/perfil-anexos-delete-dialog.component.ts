import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPerfilAnexos } from '../perfil-anexos.model';
import { PerfilAnexosService } from '../service/perfil-anexos.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './perfil-anexos-delete-dialog.component.html',
})
export class PerfilAnexosDeleteDialogComponent {
  perfilAnexos?: IPerfilAnexos;

  constructor(protected perfilAnexosService: PerfilAnexosService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.perfilAnexosService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
