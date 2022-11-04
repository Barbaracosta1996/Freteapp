import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SettingsContractsComponent } from './list/settings-contracts.component';
import { SettingsContractsDetailComponent } from './detail/settings-contracts-detail.component';
import { SettingsContractsUpdateComponent } from './update/settings-contracts-update.component';
import { SettingsContractsDeleteDialogComponent } from './delete/settings-contracts-delete-dialog.component';
import { SettingsContractsRoutingModule } from './route/settings-contracts-routing.module';

@NgModule({
  imports: [SharedModule, SettingsContractsRoutingModule],
  declarations: [
    SettingsContractsComponent,
    SettingsContractsDetailComponent,
    SettingsContractsUpdateComponent,
    SettingsContractsDeleteDialogComponent,
  ],
})
export class SettingsContractsModule {}
