import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SettingsCargaAppComponent } from './list/settings-carga-app.component';
import { SettingsCargaAppDetailComponent } from './detail/settings-carga-app-detail.component';
import { SettingsCargaAppUpdateComponent } from './update/settings-carga-app-update.component';
import { SettingsCargaAppDeleteDialogComponent } from './delete/settings-carga-app-delete-dialog.component';
import { SettingsCargaAppRoutingModule } from './route/settings-carga-app-routing.module';

@NgModule({
  imports: [SharedModule, SettingsCargaAppRoutingModule],
  declarations: [
    SettingsCargaAppComponent,
    SettingsCargaAppDetailComponent,
    SettingsCargaAppUpdateComponent,
    SettingsCargaAppDeleteDialogComponent,
  ],
})
export class SettingsCargaAppModule {}
