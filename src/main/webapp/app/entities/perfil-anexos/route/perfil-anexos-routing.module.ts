import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PerfilAnexosComponent } from '../list/perfil-anexos.component';
import { PerfilAnexosDetailComponent } from '../detail/perfil-anexos-detail.component';
import { PerfilAnexosUpdateComponent } from '../update/perfil-anexos-update.component';
import { PerfilAnexosRoutingResolveService } from './perfil-anexos-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const perfilAnexosRoute: Routes = [
  {
    path: '',
    component: PerfilAnexosComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PerfilAnexosDetailComponent,
    resolve: {
      perfilAnexos: PerfilAnexosRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PerfilAnexosUpdateComponent,
    resolve: {
      perfilAnexos: PerfilAnexosRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PerfilAnexosUpdateComponent,
    resolve: {
      perfilAnexos: PerfilAnexosRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(perfilAnexosRoute)],
  exports: [RouterModule],
})
export class PerfilAnexosRoutingModule {}
