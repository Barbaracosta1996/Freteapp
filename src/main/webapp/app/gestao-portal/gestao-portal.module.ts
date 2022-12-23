import { NgModule } from '@angular/core';
import { GestaoMainComponent } from './gestao-main/gestao-main.component';
import { GridGestaoOfertasComponent } from './gestao-main/grid-ofertas/grid-gestao-ofertas.component';
import { SharedModule } from '../shared/shared.module';
import { BreadcrumbModule } from 'primeng/breadcrumb';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { RippleModule } from 'primeng/ripple';
import { GestaoOfertasComponent } from './gestao-ofertas/gestao-ofertas.component';
import { DropdownModule } from 'primeng/dropdown';
import { CalendarModule } from 'primeng/calendar';
import { InputNumberModule } from 'primeng/inputnumber';
import { AutoCompleteModule } from 'primeng/autocomplete';
import { InputTextModule } from 'primeng/inputtext';
import { GestaoRoutingModule } from './gestao-routing.module';

@NgModule({
  declarations: [GridGestaoOfertasComponent, GestaoMainComponent, GestaoOfertasComponent],
  imports: [
    SharedModule,
    GestaoRoutingModule,
    BreadcrumbModule,
    TableModule,
    ButtonModule,
    RippleModule,
    DropdownModule,
    CalendarModule,
    InputNumberModule,
    AutoCompleteModule,
    InputTextModule,
  ],
})
export class GestaoPortalModule {}
