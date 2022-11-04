import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISettingsContracts } from '../settings-contracts.model';
import { SettingsContractsService } from '../service/settings-contracts.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './settings-contracts-delete-dialog.component.html',
})
export class SettingsContractsDeleteDialogComponent {
  settingsContracts?: ISettingsContracts;

  constructor(protected settingsContractsService: SettingsContractsService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.settingsContractsService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
