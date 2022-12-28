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
import { GestaoConexaoComponent } from './gestao-conexao/gestao-conexao.component';
import { GestaoSolicitacaoComponent } from './gestao-solicitacao/gestao-solicitacao.component';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { GestaoHomeComponent } from './gestao-home/gestao-home.component';

@NgModule({
  declarations: [
    GridGestaoOfertasComponent,
    GestaoMainComponent,
    GestaoOfertasComponent,
    GestaoConexaoComponent,
    GestaoSolicitacaoComponent,
    GestaoHomeComponent,
  ],
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
    ConfirmDialogModule,
  ],
})
export class GestaoPortalModule {}
