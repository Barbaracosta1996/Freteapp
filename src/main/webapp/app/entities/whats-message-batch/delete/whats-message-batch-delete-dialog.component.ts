import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IWhatsMessageBatch } from '../whats-message-batch.model';
import { WhatsMessageBatchService } from '../service/whats-message-batch.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './whats-message-batch-delete-dialog.component.html',
})
export class WhatsMessageBatchDeleteDialogComponent {
  whatsMessageBatch?: IWhatsMessageBatch;

  constructor(protected whatsMessageBatchService: WhatsMessageBatchService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.whatsMessageBatchService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
