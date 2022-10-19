import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PerfilAnexosComponent } from './list/perfil-anexos.component';
import { PerfilAnexosDetailComponent } from './detail/perfil-anexos-detail.component';
import { PerfilAnexosUpdateComponent } from './update/perfil-anexos-update.component';
import { PerfilAnexosDeleteDialogComponent } from './delete/perfil-anexos-delete-dialog.component';
import { PerfilAnexosRoutingModule } from './route/perfil-anexos-routing.module';

@NgModule({
  imports: [SharedModule, PerfilAnexosRoutingModule],
  declarations: [PerfilAnexosComponent, PerfilAnexosDetailComponent, PerfilAnexosUpdateComponent, PerfilAnexosDeleteDialogComponent],
})
export class PerfilAnexosModule {}
