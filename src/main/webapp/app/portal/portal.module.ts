import { NgModule } from '@angular/core';
import {SharedModule} from "../shared/shared.module";
import {PortalRoutingModule} from "./portal-routing.module";
import {PanelComponent} from "./panel/panel.component";
import { OfertaDivulgarComponent } from './oferta-divulgar/oferta-divulgar.component';
import {InputTextModule} from "primeng/inputtext";
import {ButtonModule} from "primeng/button";
import {RippleModule} from "primeng/ripple";
import { GridOfertasComponent } from './panel/grid-ofertas/grid-ofertas.component';
import {ProgressBarModule} from "primeng/progressbar";
import {TableModule} from "primeng/table";
import {MultiSelectModule} from "primeng/multiselect";
import {CalendarModule} from "primeng/calendar";
import {DropdownModule} from "primeng/dropdown";
import {InputNumberModule} from "primeng/inputnumber";
import {AutoCompleteModule} from "primeng/autocomplete";
import { PanelMenuComponent } from './panel-menu/panel-menu.component';
import {MenuModule} from "primeng/menu";
import {SidebarModule} from "primeng/sidebar";
import { RotasIndicadasComponent } from './rotas-indicadas/rotas-indicadas.component';
import { ConexaoComponent } from './conexao/conexao.component';
import {ConfirmationService, MessageService} from "primeng/api";
import {ConfirmDialogModule} from "primeng/confirmdialog";
import {ToastModule} from "primeng/toast";

@NgModule({
  declarations: [PanelComponent, OfertaDivulgarComponent, GridOfertasComponent, PanelMenuComponent, RotasIndicadasComponent, ConexaoComponent],
  imports: [
    SharedModule, PortalRoutingModule, InputTextModule, ButtonModule, RippleModule, ProgressBarModule, TableModule, MultiSelectModule, CalendarModule, DropdownModule, InputNumberModule, AutoCompleteModule, MenuModule, SidebarModule, ConfirmDialogModule, ToastModule
  ],
  providers: [ConfirmationService,MessageService]
})
export class PortalModule { }
