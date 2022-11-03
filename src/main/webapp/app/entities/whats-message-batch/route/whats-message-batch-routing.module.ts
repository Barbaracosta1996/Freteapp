import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { WhatsMessageBatchComponent } from '../list/whats-message-batch.component';
import { WhatsMessageBatchDetailComponent } from '../detail/whats-message-batch-detail.component';
import { WhatsMessageBatchUpdateComponent } from '../update/whats-message-batch-update.component';
import { WhatsMessageBatchRoutingResolveService } from './whats-message-batch-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const whatsMessageBatchRoute: Routes = [
  {
    path: '',
    component: WhatsMessageBatchComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: WhatsMessageBatchDetailComponent,
    resolve: {
      whatsMessageBatch: WhatsMessageBatchRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: WhatsMessageBatchUpdateComponent,
    resolve: {
      whatsMessageBatch: WhatsMessageBatchRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: WhatsMessageBatchUpdateComponent,
    resolve: {
      whatsMessageBatch: WhatsMessageBatchRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(whatsMessageBatchRoute)],
  exports: [RouterModule],
})
export class WhatsMessageBatchRoutingModule {}
