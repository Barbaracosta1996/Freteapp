import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { OfertasComponent } from './list/ofertas.component';
import { OfertasDetailComponent } from './detail/ofertas-detail.component';
import { OfertasUpdateComponent } from './update/ofertas-update.component';
import { OfertasDeleteDialogComponent } from './delete/ofertas-delete-dialog.component';
import { OfertasRoutingModule } from './route/ofertas-routing.module';
import { DropdownModule } from 'primeng/dropdown';

@NgModule({
  imports: [SharedModule, OfertasRoutingModule, DropdownModule],
  declarations: [OfertasComponent, OfertasDetailComponent, OfertasUpdateComponent, OfertasDeleteDialogComponent],
})
export class OfertasModule {}
