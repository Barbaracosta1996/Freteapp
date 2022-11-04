import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { SettingsContractsFormService, SettingsContractsFormGroup } from './settings-contracts-form.service';
import { ISettingsContracts } from '../settings-contracts.model';
import { SettingsContractsService } from '../service/settings-contracts.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-settings-contracts-update',
  templateUrl: './settings-contracts-update.component.html',
})
export class SettingsContractsUpdateComponent implements OnInit {
  isSaving = false;
  settingsContracts: ISettingsContracts | null = null;

  editForm: SettingsContractsFormGroup = this.settingsContractsFormService.createSettingsContractsFormGroup();

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected settingsContractsService: SettingsContractsService,
    protected settingsContractsFormService: SettingsContractsFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ settingsContracts }) => {
      this.settingsContracts = settingsContracts;
      if (settingsContracts) {
        this.updateForm(settingsContracts);
      }
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('freteappApp.error', { message: err.message })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const settingsContracts = this.settingsContractsFormService.getSettingsContracts(this.editForm);
    if (settingsContracts.id !== null) {
      this.subscribeToSaveResponse(this.settingsContractsService.update(settingsContracts));
    } else {
      this.subscribeToSaveResponse(this.settingsContractsService.create(settingsContracts));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISettingsContracts>>): void {
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

  protected updateForm(settingsContracts: ISettingsContracts): void {
    this.settingsContracts = settingsContracts;
    this.settingsContractsFormService.resetForm(this.editForm, settingsContracts);
  }
}
