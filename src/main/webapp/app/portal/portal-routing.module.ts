import {NgModule} from "@angular/core";
import {RouterModule} from "@angular/router";
import {Authority} from "../config/authority.constants";
import {UserRouteAccessService} from "../core/auth/user-route-access.service";
import {PanelComponent} from "./panel/panel.component";
import {PortalResolveService} from "./portal-resolve.service";
import {OfertaDivulgarComponent} from "./oferta-divulgar/oferta-divulgar.component";
import {DivulgarResolveService} from "./oferta-divulgar/divulgar-resolve.service";
import {
  NearRoutingResolveService,
  OfertasRoutingResolveService
} from "../entities/ofertas/route/ofertas-routing-resolve.service";
import {RotasIndicadasComponent} from "./rotas-indicadas/rotas-indicadas.component";
import {ConexaoComponent} from "./conexao/conexao.component";
import {ASC} from "../config/navigation.constants";

@NgModule({
  imports: [
    RouterModule.forChild(
      [
        {
          path: '',
          data: {
            authorities: [Authority.USER],
          },
          resolve: {
            perfil: PortalResolveService,
          },
          canActivate: [UserRouteAccessService],
          component: PanelComponent
        },
        {
          path: 'nova-oferta/:tipo',
          data: {
            authorities: [Authority.USER],
          },
          resolve: {
            ofertas: DivulgarResolveService,
          },
          canActivate: [UserRouteAccessService],
          component: OfertaDivulgarComponent
        },
        {
          path: 'oferta/edit/:id',
          data: {
            authorities: [Authority.USER],
          },
          resolve: {
            ofertas: OfertasRoutingResolveService,
          },
          canActivate: [UserRouteAccessService],
          component: OfertaDivulgarComponent
        },
        {
          path: 'indicacao/:id',
          data: {
            authorities: [Authority.USER],
          },
          resolve: {
            ofertas: NearRoutingResolveService,
          },
          canActivate: [UserRouteAccessService],
          component: RotasIndicadasComponent
        },
        {
          path: 'conexao',
          data: {
            authorities: [Authority.USER],
            defaultSort: 'id,' + ASC
          },
          canActivate: [UserRouteAccessService],
          component: ConexaoComponent
        },
        {
          path: '404',
          redirectTo: 'criar-perfil'
        },
      ],
    ),
  ],
  exports: [RouterModule],
})
export class PortalRoutingModule {}
