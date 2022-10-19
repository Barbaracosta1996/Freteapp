import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { FrotaComponent } from './list/frota.component';
import { FrotaDetailComponent } from './detail/frota-detail.component';
import { FrotaUpdateComponent } from './update/frota-update.component';
import { FrotaDeleteDialogComponent } from './delete/frota-delete-dialog.component';
import { FrotaRoutingModule } from './route/frota-routing.module';

@NgModule({
  imports: [SharedModule, FrotaRoutingModule],
  declarations: [FrotaComponent, FrotaDetailComponent, FrotaUpdateComponent, FrotaDeleteDialogComponent],
})
export class FrotaModule {}
