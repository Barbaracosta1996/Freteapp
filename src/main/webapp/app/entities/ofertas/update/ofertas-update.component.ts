import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { OfertasFormService, OfertasFormGroup } from './ofertas-form.service';
import { IOfertas } from '../ofertas.model';
import { OfertasService } from '../service/ofertas.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IPerfil } from 'app/entities/perfil/perfil.model';
import { PerfilService } from 'app/entities/perfil/service/perfil.service';
import { TipoCarga } from 'app/entities/enumerations/tipo-carga.model';
import { StatusOferta } from 'app/entities/enumerations/status-oferta.model';
import { TipoOferta } from 'app/entities/enumerations/tipo-oferta.model';
import { TipoTransporteOferta } from 'app/entities/enumerations/tipo-transporte-oferta.model';

@Component({
  selector: 'jhi-ofertas-update',
  templateUrl: './ofertas-update.component.html',
})
export class OfertasUpdateComponent implements OnInit {
  isSaving = false;
  ofertas: IOfertas | null = null;
  tipoCargaValues = Object.keys(TipoCarga);
  statusOfertaValues = Object.keys(StatusOferta);
  tipoOfertaValues = Object.keys(TipoOferta);
  tipoTransporteOfertaValues = Object.keys(TipoTransporteOferta);

  perfilsSharedCollection: IPerfil[] = [];

  editForm: OfertasFormGroup = this.ofertasFormService.createOfertasFormGroup();

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected ofertasService: OfertasService,
    protected ofertasFormService: OfertasFormService,
    protected perfilService: PerfilService,
    protected activatedRoute: ActivatedRoute
  ) {}

  comparePerfil = (o1: IPerfil | null, o2: IPerfil | null): boolean => this.perfilService.comparePerfil(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ofertas }) => {
      this.ofertas = ofertas;
      if (ofertas) {
        this.updateForm(ofertas);
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

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const ofertas = this.ofertasFormService.getOfertas(this.editForm);
    if (ofertas.id !== null) {
      this.subscribeToSaveResponse(this.ofertasService.update(ofertas));
    } else {
      this.subscribeToSaveResponse(this.ofertasService.create(ofertas));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOfertas>>): void {
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

  protected updateForm(ofertas: IOfertas): void {
    this.ofertas = ofertas;
    this.ofertasFormService.resetForm(this.editForm, ofertas);

    this.perfilsSharedCollection = this.perfilService.addPerfilToCollectionIfMissing<IPerfil>(this.perfilsSharedCollection, ofertas.perfil);
  }

  protected loadRelationshipsOptions(): void {
    this.perfilService
      .query()
      .pipe(map((res: HttpResponse<IPerfil[]>) => res.body ?? []))
      .pipe(map((perfils: IPerfil[]) => this.perfilService.addPerfilToCollectionIfMissing<IPerfil>(perfils, this.ofertas?.perfil)))
      .subscribe((perfils: IPerfil[]) => (this.perfilsSharedCollection = perfils));
  }
}
