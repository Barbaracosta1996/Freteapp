import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FrotaComponent } from '../list/frota.component';
import { FrotaDetailComponent } from '../detail/frota-detail.component';
import { FrotaUpdateComponent } from '../update/frota-update.component';
import { FrotaRoutingResolveService } from './frota-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const frotaRoute: Routes = [
  {
    path: '',
    component: FrotaComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FrotaDetailComponent,
    resolve: {
      frota: FrotaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FrotaUpdateComponent,
    resolve: {
      frota: FrotaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FrotaUpdateComponent,
    resolve: {
      frota: FrotaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(frotaRoute)],
  exports: [RouterModule],
})
export class FrotaRoutingModule {}
