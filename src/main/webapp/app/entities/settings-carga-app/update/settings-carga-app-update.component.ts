import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { SettingsCargaAppFormService, SettingsCargaAppFormGroup } from './settings-carga-app-form.service';
import { ISettingsCargaApp } from '../settings-carga-app.model';
import { SettingsCargaAppService } from '../service/settings-carga-app.service';

@Component({
  selector: 'jhi-settings-carga-app-update',
  templateUrl: './settings-carga-app-update.component.html',
})
export class SettingsCargaAppUpdateComponent implements OnInit {
  isSaving = false;
  settingsCargaApp: ISettingsCargaApp | null = null;

  editForm: SettingsCargaAppFormGroup = this.settingsCargaAppFormService.createSettingsCargaAppFormGroup();

  constructor(
    protected settingsCargaAppService: SettingsCargaAppService,
    protected settingsCargaAppFormService: SettingsCargaAppFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ settingsCargaApp }) => {
      this.settingsCargaApp = settingsCargaApp;
      if (settingsCargaApp) {
        this.updateForm(settingsCargaApp);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const settingsCargaApp = this.settingsCargaAppFormService.getSettingsCargaApp(this.editForm);
    if (settingsCargaApp.id !== null) {
      this.subscribeToSaveResponse(this.settingsCargaAppService.update(settingsCargaApp));
    } else {
      this.subscribeToSaveResponse(this.settingsCargaAppService.create(settingsCargaApp));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISettingsCargaApp>>): void {
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

  protected updateForm(settingsCargaApp: ISettingsCargaApp): void {
    this.settingsCargaApp = settingsCargaApp;
    this.settingsCargaAppFormService.resetForm(this.editForm, settingsCargaApp);
  }
}
