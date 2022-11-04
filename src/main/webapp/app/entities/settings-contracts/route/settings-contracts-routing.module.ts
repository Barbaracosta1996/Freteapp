import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SettingsContractsComponent } from '../list/settings-contracts.component';
import { SettingsContractsDetailComponent } from '../detail/settings-contracts-detail.component';
import { SettingsContractsUpdateComponent } from '../update/settings-contracts-update.component';
import { SettingsContractsRoutingResolveService } from './settings-contracts-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const settingsContractsRoute: Routes = [
  {
    path: '',
    component: SettingsContractsComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SettingsContractsDetailComponent,
    resolve: {
      settingsContracts: SettingsContractsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SettingsContractsUpdateComponent,
    resolve: {
      settingsContracts: SettingsContractsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SettingsContractsUpdateComponent,
    resolve: {
      settingsContracts: SettingsContractsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(settingsContractsRoute)],
  exports: [RouterModule],
})
export class SettingsContractsRoutingModule {}
