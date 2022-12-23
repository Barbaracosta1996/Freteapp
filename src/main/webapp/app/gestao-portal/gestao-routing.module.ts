import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { Authority } from '../config/authority.constants';
import { UserRouteAccessService } from '../core/auth/user-route-access.service';
import { ErrorComponent } from '../layouts/error/error.component';
import { GestaoMainComponent } from './gestao-main/gestao-main.component';
import { OfertaDivulgarComponent } from '../portal/oferta-divulgar/oferta-divulgar.component';
import { OfertasRoutingResolveService } from '../entities/ofertas/route/ofertas-routing-resolve.service';
import { GestaoOfertasComponent } from './gestao-ofertas/gestao-ofertas.component';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: '',
        data: {
          authorities: [Authority.ADMIN],
        },
        canActivate: [UserRouteAccessService],
        component: GestaoMainComponent,
      },
      {
        path: 'nova-oferta/:tipo',
        data: {
          authorities: [Authority.ADMIN],
        },
        resolve: {
          ofertas: OfertasRoutingResolveService,
        },
        canActivate: [UserRouteAccessService],
        component: GestaoOfertasComponent,
      },
      {
        path: 'oferta/edit/:id',
        data: {
          authorities: [Authority.ADMIN],
        },
        resolve: {
          ofertas: OfertasRoutingResolveService,
        },
        canActivate: [UserRouteAccessService],
        component: GestaoOfertasComponent,
      },
      {
        path: '404',
        component: ErrorComponent,
      },
    ]),
  ],
  exports: [RouterModule],
})
export class GestaoRoutingModule {}
