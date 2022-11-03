import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IWhatsMessageBatch } from '../whats-message-batch.model';

@Component({
  selector: 'jhi-whats-message-batch-detail',
  templateUrl: './whats-message-batch-detail.component.html',
})
export class WhatsMessageBatchDetailComponent implements OnInit {
  whatsMessageBatch: IWhatsMessageBatch | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ whatsMessageBatch }) => {
      this.whatsMessageBatch = whatsMessageBatch;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
