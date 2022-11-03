import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SettingsCargaAppComponent } from '../list/settings-carga-app.component';
import { SettingsCargaAppDetailComponent } from '../detail/settings-carga-app-detail.component';
import { SettingsCargaAppUpdateComponent } from '../update/settings-carga-app-update.component';
import { SettingsCargaAppRoutingResolveService } from './settings-carga-app-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const settingsCargaAppRoute: Routes = [
  {
    path: '',
    component: SettingsCargaAppComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SettingsCargaAppDetailComponent,
    resolve: {
      settingsCargaApp: SettingsCargaAppRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SettingsCargaAppUpdateComponent,
    resolve: {
      settingsCargaApp: SettingsCargaAppRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SettingsCargaAppUpdateComponent,
    resolve: {
      settingsCargaApp: SettingsCargaAppRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(settingsCargaAppRoute)],
  exports: [RouterModule],
})
export class SettingsCargaAppRoutingModule {}
