import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { OfertasComponent } from '../list/ofertas.component';
import { OfertasDetailComponent } from '../detail/ofertas-detail.component';
import { OfertasUpdateComponent } from '../update/ofertas-update.component';
import { OfertasRoutingResolveService } from './ofertas-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const ofertasRoute: Routes = [
  {
    path: '',
    component: OfertasComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: OfertasDetailComponent,
    resolve: {
      ofertas: OfertasRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: OfertasUpdateComponent,
    resolve: {
      ofertas: OfertasRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: OfertasUpdateComponent,
    resolve: {
      ofertas: OfertasRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(ofertasRoute)],
  exports: [RouterModule],
})
export class OfertasRoutingModule {}
