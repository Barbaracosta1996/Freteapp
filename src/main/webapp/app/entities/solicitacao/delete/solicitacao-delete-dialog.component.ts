import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISolicitacao } from '../solicitacao.model';
import { SolicitacaoService } from '../service/solicitacao.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './solicitacao-delete-dialog.component.html',
})
export class SolicitacaoDeleteDialogComponent {
  solicitacao?: ISolicitacao;

  constructor(protected solicitacaoService: SolicitacaoService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.solicitacaoService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
