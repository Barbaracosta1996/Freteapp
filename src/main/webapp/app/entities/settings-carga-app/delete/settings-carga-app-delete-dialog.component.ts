import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISettingsCargaApp } from '../settings-carga-app.model';
import { SettingsCargaAppService } from '../service/settings-carga-app.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './settings-carga-app-delete-dialog.component.html',
})
export class SettingsCargaAppDeleteDialogComponent {
  settingsCargaApp?: ISettingsCargaApp;

  constructor(protected settingsCargaAppService: SettingsCargaAppService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.settingsCargaAppService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
