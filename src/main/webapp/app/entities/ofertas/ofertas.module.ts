import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { OfertasComponent } from './list/ofertas.component';
import { OfertasDetailComponent } from './detail/ofertas-detail.component';
import { OfertasUpdateComponent } from './update/ofertas-update.component';
import { OfertasDeleteDialogComponent } from './delete/ofertas-delete-dialog.component';
import { OfertasRoutingModule } from './route/ofertas-routing.module';

@NgModule({
  imports: [SharedModule, OfertasRoutingModule],
  declarations: [OfertasComponent, OfertasDetailComponent, OfertasUpdateComponent, OfertasDeleteDialogComponent],
})
export class OfertasModule {}
