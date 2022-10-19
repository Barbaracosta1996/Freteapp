import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { FrotaFormService, FrotaFormGroup } from './frota-form.service';
import { IFrota } from '../frota.model';
import { FrotaService } from '../service/frota.service';
import { IPerfil } from 'app/entities/perfil/perfil.model';
import { PerfilService } from 'app/entities/perfil/service/perfil.service';
import { ICategoriaVeiculo } from 'app/entities/categoria-veiculo/categoria-veiculo.model';
import { CategoriaVeiculoService } from 'app/entities/categoria-veiculo/service/categoria-veiculo.service';
import { TipoCategoria } from 'app/entities/enumerations/tipo-categoria.model';

@Component({
  selector: 'jhi-frota-update',
  templateUrl: './frota-update.component.html',
})
export class FrotaUpdateComponent implements OnInit {
  isSaving = false;
  frota: IFrota | null = null;
  tipoCategoriaValues = Object.keys(TipoCategoria);

  perfilsSharedCollection: IPerfil[] = [];
  categoriaVeiculosSharedCollection: ICategoriaVeiculo[] = [];

  editForm: FrotaFormGroup = this.frotaFormService.createFrotaFormGroup();

  constructor(
    protected frotaService: FrotaService,
    protected frotaFormService: FrotaFormService,
    protected perfilService: PerfilService,
    protected categoriaVeiculoService: CategoriaVeiculoService,
    protected activatedRoute: ActivatedRoute
  ) {}

  comparePerfil = (o1: IPerfil | null, o2: IPerfil | null): boolean => this.perfilService.comparePerfil(o1, o2);

  compareCategoriaVeiculo = (o1: ICategoriaVeiculo | null, o2: ICategoriaVeiculo | null): boolean =>
    this.categoriaVeiculoService.compareCategoriaVeiculo(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ frota }) => {
      this.frota = frota;
      if (frota) {
        this.updateForm(frota);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const frota = this.frotaFormService.getFrota(this.editForm);
    if (frota.id !== null) {
      this.subscribeToSaveResponse(this.frotaService.update(frota));
    } else {
      this.subscribeToSaveResponse(this.frotaService.create(frota));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFrota>>): void {
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

  protected updateForm(frota: IFrota): void {
    this.frota = frota;
    this.frotaFormService.resetForm(this.editForm, frota);

    this.perfilsSharedCollection = this.perfilService.addPerfilToCollectionIfMissing<IPerfil>(this.perfilsSharedCollection, frota.perfil);
    this.categoriaVeiculosSharedCollection = this.categoriaVeiculoService.addCategoriaVeiculoToCollectionIfMissing<ICategoriaVeiculo>(
      this.categoriaVeiculosSharedCollection,
      frota.categoriaVeiculo
    );
  }

  protected loadRelationshipsOptions(): void {
    this.perfilService
      .query()
      .pipe(map((res: HttpResponse<IPerfil[]>) => res.body ?? []))
      .pipe(map((perfils: IPerfil[]) => this.perfilService.addPerfilToCollectionIfMissing<IPerfil>(perfils, this.frota?.perfil)))
      .subscribe((perfils: IPerfil[]) => (this.perfilsSharedCollection = perfils));

    this.categoriaVeiculoService
      .query()
      .pipe(map((res: HttpResponse<ICategoriaVeiculo[]>) => res.body ?? []))
      .pipe(
        map((categoriaVeiculos: ICategoriaVeiculo[]) =>
          this.categoriaVeiculoService.addCategoriaVeiculoToCollectionIfMissing<ICategoriaVeiculo>(
            categoriaVeiculos,
            this.frota?.categoriaVeiculo
          )
        )
      )
      .subscribe((categoriaVeiculos: ICategoriaVeiculo[]) => (this.categoriaVeiculosSharedCollection = categoriaVeiculos));
  }
}
