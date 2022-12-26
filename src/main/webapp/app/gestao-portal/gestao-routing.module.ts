import { Injectable, NgModule } from '@angular/core';
import { ActivatedRouteSnapshot, Resolve, Router, RouterModule } from '@angular/router';
import { Authority } from '../config/authority.constants';
import { UserRouteAccessService } from '../core/auth/user-route-access.service';
import { ErrorComponent } from '../layouts/error/error.component';
import { GestaoMainComponent } from './gestao-main/gestao-main.component';
import { OfertaDivulgarComponent } from '../portal/oferta-divulgar/oferta-divulgar.component';
import { NearRoutingResolveService, OfertasRoutingResolveService } from '../entities/ofertas/route/ofertas-routing-resolve.service';
import { GestaoOfertasComponent } from './gestao-ofertas/gestao-ofertas.component';
import { RotasIndicadasComponent } from '../portal/rotas-indicadas/rotas-indicadas.component';
import { GestaoSolicitacaoComponent } from './gestao-solicitacao/gestao-solicitacao.component';
import { IOfertas } from '../entities/ofertas/ofertas.model';
import { OfertasService } from '../entities/ofertas/service/ofertas.service';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';
import { HttpResponse } from '@angular/common/http';

@Injectable({ providedIn: 'root' })
export class GestaoConexaoRoutingResolveService implements Resolve<IOfertas[]> {
  constructor(protected service: OfertasService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IOfertas[]> {
    const id = route.params['id'];
    if (id) {
      return this.service.query().pipe(
        mergeMap((ofertas: HttpResponse<IOfertas[]>) => {
          if (ofertas.body) {
            return of(ofertas.body);
          } else {
            return EMPTY;
          }
        })
      );
    }
    return of([]);
  }
}

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
        path: 'indicacao/:id',
        data: {
          authorities: [Authority.ADMIN],
        },
        resolve: {
          ofertas: GestaoConexaoRoutingResolveService,
        },
        canActivate: [UserRouteAccessService],
        component: GestaoSolicitacaoComponent,
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
