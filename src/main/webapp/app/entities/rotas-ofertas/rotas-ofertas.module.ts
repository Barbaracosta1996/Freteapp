import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { RotasOfertasComponent } from './list/rotas-ofertas.component';
import { RotasOfertasDetailComponent } from './detail/rotas-ofertas-detail.component';
import { RotasOfertasUpdateComponent } from './update/rotas-ofertas-update.component';
import { RotasOfertasDeleteDialogComponent } from './delete/rotas-ofertas-delete-dialog.component';
import { RotasOfertasRoutingModule } from './route/rotas-ofertas-routing.module';

@NgModule({
  imports: [SharedModule, RotasOfertasRoutingModule],
  declarations: [RotasOfertasComponent, RotasOfertasDetailComponent, RotasOfertasUpdateComponent, RotasOfertasDeleteDialogComponent],
})
export class RotasOfertasModule {}
