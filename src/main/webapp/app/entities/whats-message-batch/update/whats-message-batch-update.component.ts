import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { WhatsMessageBatchFormService, WhatsMessageBatchFormGroup } from './whats-message-batch-form.service';
import { IWhatsMessageBatch } from '../whats-message-batch.model';
import { WhatsMessageBatchService } from '../service/whats-message-batch.service';
import { WhatsAppType } from 'app/entities/enumerations/whats-app-type.model';
import { WhatsStatus } from 'app/entities/enumerations/whats-status.model';
import { TipoOferta } from 'app/entities/enumerations/tipo-oferta.model';

@Component({
  selector: 'jhi-whats-message-batch-update',
  templateUrl: './whats-message-batch-update.component.html',
})
export class WhatsMessageBatchUpdateComponent implements OnInit {
  isSaving = false;
  whatsMessageBatch: IWhatsMessageBatch | null = null;
  whatsAppTypeValues = Object.keys(WhatsAppType);
  whatsStatusValues = Object.keys(WhatsStatus);
  tipoOfertaValues = Object.keys(TipoOferta);

  editForm: WhatsMessageBatchFormGroup = this.whatsMessageBatchFormService.createWhatsMessageBatchFormGroup();

  constructor(
    protected whatsMessageBatchService: WhatsMessageBatchService,
    protected whatsMessageBatchFormService: WhatsMessageBatchFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ whatsMessageBatch }) => {
      this.whatsMessageBatch = whatsMessageBatch;
      if (whatsMessageBatch) {
        this.updateForm(whatsMessageBatch);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const whatsMessageBatch = this.whatsMessageBatchFormService.getWhatsMessageBatch(this.editForm);
    if (whatsMessageBatch.id !== null) {
      this.subscribeToSaveResponse(this.whatsMessageBatchService.update(whatsMessageBatch));
    } else {
      this.subscribeToSaveResponse(this.whatsMessageBatchService.create(whatsMessageBatch));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IWhatsMessageBatch>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(whatsMessageBatch: IWhatsMessageBatch): void {
    this.whatsMessageBatch = whatsMessageBatch;
    this.whatsMessageBatchFormService.resetForm(this.editForm, whatsMessageBatch);
  }
}
