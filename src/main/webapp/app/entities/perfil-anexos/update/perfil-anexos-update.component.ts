import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { PerfilAnexosFormService, PerfilAnexosFormGroup } from './perfil-anexos-form.service';
import { IPerfilAnexos } from '../perfil-anexos.model';
import { PerfilAnexosService } from '../service/perfil-anexos.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IPerfil } from 'app/entities/perfil/perfil.model';
import { PerfilService } from 'app/entities/perfil/service/perfil.service';
import { TipoPerfilDocumento } from 'app/entities/enumerations/tipo-perfil-documento.model';

@Component({
  selector: 'jhi-perfil-anexos-update',
  templateUrl: './perfil-anexos-update.component.html',
})
export class PerfilAnexosUpdateComponent implements OnInit {
  isSaving = false;
  perfilAnexos: IPerfilAnexos | null = null;
  tipoPerfilDocumentoValues = Object.keys(TipoPerfilDocumento);

  perfilsSharedCollection: IPerfil[] = [];

  editForm: PerfilAnexosFormGroup = this.perfilAnexosFormService.createPerfilAnexosFormGroup();

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected perfilAnexosService: PerfilAnexosService,
    protected perfilAnexosFormService: PerfilAnexosFormService,
    protected perfilService: PerfilService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute
  ) {}

  comparePerfil = (o1: IPerfil | null, o2: IPerfil | null): boolean => this.perfilService.comparePerfil(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ perfilAnexos }) => {
      this.perfilAnexos = perfilAnexos;
      if (perfilAnexos) {
        this.updateForm(perfilAnexos);
      }

      this.loadRelationshipsOptions();
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

  clearInputImage(field: string, fieldContentType: string, idInput: string): void {
    this.editForm.patchValue({
      [field]: null,
      [fieldContentType]: null,
    });
    if (idInput && this.elementRef.nativeElement.querySelector('#' + idInput)) {
      this.elementRef.nativeElement.querySelector('#' + idInput).value = null;
    }
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const perfilAnexos = this.perfilAnexosFormService.getPerfilAnexos(this.editForm);
    if (perfilAnexos.id !== null) {
      this.subscribeToSaveResponse(this.perfilAnexosService.update(perfilAnexos));
    } else {
      this.subscribeToSaveResponse(this.perfilAnexosService.create(perfilAnexos));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPerfilAnexos>>): void {
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

  protected updateForm(perfilAnexos: IPerfilAnexos): void {
    this.perfilAnexos = perfilAnexos;
    this.perfilAnexosFormService.resetForm(this.editForm, perfilAnexos);

    this.perfilsSharedCollection = this.perfilService.addPerfilToCollectionIfMissing<IPerfil>(
      this.perfilsSharedCollection,
      perfilAnexos.perfil
    );
  }

  protected loadRelationshipsOptions(): void {
    this.perfilService
      .query()
      .pipe(map((res: HttpResponse<IPerfil[]>) => res.body ?? []))
      .pipe(map((perfils: IPerfil[]) => this.perfilService.addPerfilToCollectionIfMissing<IPerfil>(perfils, this.perfilAnexos?.perfil)))
      .subscribe((perfils: IPerfil[]) => (this.perfilsSharedCollection = perfils));
  }
}
