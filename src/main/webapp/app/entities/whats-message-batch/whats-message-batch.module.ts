import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { WhatsMessageBatchComponent } from './list/whats-message-batch.component';
import { WhatsMessageBatchDetailComponent } from './detail/whats-message-batch-detail.component';
import { WhatsMessageBatchUpdateComponent } from './update/whats-message-batch-update.component';
import { WhatsMessageBatchDeleteDialogComponent } from './delete/whats-message-batch-delete-dialog.component';
import { WhatsMessageBatchRoutingModule } from './route/whats-message-batch-routing.module';

@NgModule({
  imports: [SharedModule, WhatsMessageBatchRoutingModule],
  declarations: [
    WhatsMessageBatchComponent,
    WhatsMessageBatchDetailComponent,
    WhatsMessageBatchUpdateComponent,
    WhatsMessageBatchDeleteDialogComponent,
  ],
})
export class WhatsMessageBatchModule {}
