import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { SolicitacaoFormService, SolicitacaoFormGroup } from './solicitacao-form.service';
import { ISolicitacao } from '../solicitacao.model';
import { SolicitacaoService } from '../service/solicitacao.service';
import { IOfertas } from 'app/entities/ofertas/ofertas.model';
import { OfertasService } from 'app/entities/ofertas/service/ofertas.service';
import { IPerfil } from 'app/entities/perfil/perfil.model';
import { PerfilService } from 'app/entities/perfil/service/perfil.service';
import { AnwserStatus } from 'app/entities/enumerations/anwser-status.model';
import { StatusSolicitacao } from 'app/entities/enumerations/status-solicitacao.model';

@Component({
  selector: 'jhi-solicitacao-update',
  templateUrl: './solicitacao-update.component.html',
})
export class SolicitacaoUpdateComponent implements OnInit {
  isSaving = false;
  solicitacao: ISolicitacao | null = null;
  anwserStatusValues = Object.keys(AnwserStatus);
  statusSolicitacaoValues = Object.keys(StatusSolicitacao);

  ofertasSharedCollection: IOfertas[] = [];
  perfilsSharedCollection: IPerfil[] = [];

  editForm: SolicitacaoFormGroup = this.solicitacaoFormService.createSolicitacaoFormGroup();

  constructor(
    protected solicitacaoService: SolicitacaoService,
    protected solicitacaoFormService: SolicitacaoFormService,
    protected ofertasService: OfertasService,
    protected perfilService: PerfilService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareOfertas = (o1: IOfertas | null, o2: IOfertas | null): boolean => this.ofertasService.compareOfertas(o1, o2);

  comparePerfil = (o1: IPerfil | null, o2: IPerfil | null): boolean => this.perfilService.comparePerfil(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ solicitacao }) => {
      this.solicitacao = solicitacao;
      if (solicitacao) {
        this.updateForm(solicitacao);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const solicitacao = this.solicitacaoFormService.getSolicitacao(this.editForm);
    if (solicitacao.id !== null) {
      this.subscribeToSaveResponse(this.solicitacaoService.update(solicitacao));
    } else {
      this.subscribeToSaveResponse(this.solicitacaoService.create(solicitacao));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISolicitacao>>): void {
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

  protected updateForm(solicitacao: ISolicitacao): void {
    this.solicitacao = solicitacao;
    this.solicitacaoFormService.resetForm(this.editForm, solicitacao);

    this.ofertasSharedCollection = this.ofertasService.addOfertasToCollectionIfMissing<IOfertas>(
      this.ofertasSharedCollection,
      solicitacao.ofertas,
      solicitacao.minhaOferta
    );
    this.perfilsSharedCollection = this.perfilService.addPerfilToCollectionIfMissing<IPerfil>(
      this.perfilsSharedCollection,
      solicitacao.perfil,
      solicitacao.requestedPerfil
    );
  }

  protected loadRelationshipsOptions(): void {
    this.ofertasService
      .query()
      .pipe(map((res: HttpResponse<IOfertas[]>) => res.body ?? []))
      .pipe(
        map((ofertas: IOfertas[]) =>
          this.ofertasService.addOfertasToCollectionIfMissing<IOfertas>(ofertas, this.solicitacao?.ofertas, this.solicitacao?.minhaOferta)
        )
      )
      .subscribe((ofertas: IOfertas[]) => (this.ofertasSharedCollection = ofertas));

    this.perfilService
      .query()
      .pipe(map((res: HttpResponse<IPerfil[]>) => res.body ?? []))
      .pipe(
        map((perfils: IPerfil[]) =>
          this.perfilService.addPerfilToCollectionIfMissing<IPerfil>(perfils, this.solicitacao?.perfil, this.solicitacao?.requestedPerfil)
        )
      )
      .subscribe((perfils: IPerfil[]) => (this.perfilsSharedCollection = perfils));
  }
}
