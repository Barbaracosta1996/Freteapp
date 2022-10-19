import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { RotasOfertasFormService, RotasOfertasFormGroup } from './rotas-ofertas-form.service';
import { IRotasOfertas } from '../rotas-ofertas.model';
import { RotasOfertasService } from '../service/rotas-ofertas.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IOfertas } from 'app/entities/ofertas/ofertas.model';
import { OfertasService } from 'app/entities/ofertas/service/ofertas.service';

@Component({
  selector: 'jhi-rotas-ofertas-update',
  templateUrl: './rotas-ofertas-update.component.html',
})
export class RotasOfertasUpdateComponent implements OnInit {
  isSaving = false;
  rotasOfertas: IRotasOfertas | null = null;

  ofertasSharedCollection: IOfertas[] = [];

  editForm: RotasOfertasFormGroup = this.rotasOfertasFormService.createRotasOfertasFormGroup();

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected rotasOfertasService: RotasOfertasService,
    protected rotasOfertasFormService: RotasOfertasFormService,
    protected ofertasService: OfertasService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareOfertas = (o1: IOfertas | null, o2: IOfertas | null): boolean => this.ofertasService.compareOfertas(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ rotasOfertas }) => {
      this.rotasOfertas = rotasOfertas;
      if (rotasOfertas) {
        this.updateForm(rotasOfertas);
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
    const rotasOfertas = this.rotasOfertasFormService.getRotasOfertas(this.editForm);
    if (rotasOfertas.id !== null) {
      this.subscribeToSaveResponse(this.rotasOfertasService.update(rotasOfertas));
    } else {
      this.subscribeToSaveResponse(this.rotasOfertasService.create(rotasOfertas));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRotasOfertas>>): void {
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

  protected updateForm(rotasOfertas: IRotasOfertas): void {
    this.rotasOfertas = rotasOfertas;
    this.rotasOfertasFormService.resetForm(this.editForm, rotasOfertas);

    this.ofertasSharedCollection = this.ofertasService.addOfertasToCollectionIfMissing<IOfertas>(
      this.ofertasSharedCollection,
      rotasOfertas.ofertas
    );
  }

  protected loadRelationshipsOptions(): void {
    this.ofertasService
      .query()
      .pipe(map((res: HttpResponse<IOfertas[]>) => res.body ?? []))
      .pipe(
        map((ofertas: IOfertas[]) => this.ofertasService.addOfertasToCollectionIfMissing<IOfertas>(ofertas, this.rotasOfertas?.ofertas))
      )
      .subscribe((ofertas: IOfertas[]) => (this.ofertasSharedCollection = ofertas));
  }
}
