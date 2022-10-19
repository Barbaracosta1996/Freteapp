import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { CategoriaVeiculoFormService, CategoriaVeiculoFormGroup } from './categoria-veiculo-form.service';
import { ICategoriaVeiculo } from '../categoria-veiculo.model';
import { CategoriaVeiculoService } from '../service/categoria-veiculo.service';

@Component({
  selector: 'jhi-categoria-veiculo-update',
  templateUrl: './categoria-veiculo-update.component.html',
})
export class CategoriaVeiculoUpdateComponent implements OnInit {
  isSaving = false;
  categoriaVeiculo: ICategoriaVeiculo | null = null;

  editForm: CategoriaVeiculoFormGroup = this.categoriaVeiculoFormService.createCategoriaVeiculoFormGroup();

  constructor(
    protected categoriaVeiculoService: CategoriaVeiculoService,
    protected categoriaVeiculoFormService: CategoriaVeiculoFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ categoriaVeiculo }) => {
      this.categoriaVeiculo = categoriaVeiculo;
      if (categoriaVeiculo) {
        this.updateForm(categoriaVeiculo);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const categoriaVeiculo = this.categoriaVeiculoFormService.getCategoriaVeiculo(this.editForm);
    if (categoriaVeiculo.id !== null) {
      this.subscribeToSaveResponse(this.categoriaVeiculoService.update(categoriaVeiculo));
    } else {
      this.subscribeToSaveResponse(this.categoriaVeiculoService.create(categoriaVeiculo));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICategoriaVeiculo>>): void {
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

  protected updateForm(categoriaVeiculo: ICategoriaVeiculo): void {
    this.categoriaVeiculo = categoriaVeiculo;
    this.categoriaVeiculoFormService.resetForm(this.editForm, categoriaVeiculo);
  }
}
