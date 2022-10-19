import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { RotasOfertasComponent } from '../list/rotas-ofertas.component';
import { RotasOfertasDetailComponent } from '../detail/rotas-ofertas-detail.component';
import { RotasOfertasUpdateComponent } from '../update/rotas-ofertas-update.component';
import { RotasOfertasRoutingResolveService } from './rotas-ofertas-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const rotasOfertasRoute: Routes = [
  {
    path: '',
    component: RotasOfertasComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RotasOfertasDetailComponent,
    resolve: {
      rotasOfertas: RotasOfertasRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RotasOfertasUpdateComponent,
    resolve: {
      rotasOfertas: RotasOfertasRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RotasOfertasUpdateComponent,
    resolve: {
      rotasOfertas: RotasOfertasRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(rotasOfertasRoute)],
  exports: [RouterModule],
})
export class RotasOfertasRoutingModule {}
