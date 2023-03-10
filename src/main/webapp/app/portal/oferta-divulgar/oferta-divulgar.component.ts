import { Component, OnInit } from '@angular/core';
import { IOfertas } from '../../entities/ofertas/ofertas.model';
import { IPerfil } from '../../entities/perfil/perfil.model';
import { OfertasFormGroup, OfertasFormService } from '../../entities/ofertas/update/ofertas-form.service';
import { DataUtils } from '../../core/util/data-util.service';
import { OfertasService } from '../../entities/ofertas/service/ofertas.service';
import { PerfilService } from '../../entities/perfil/service/perfil.service';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { HttpResponse } from '@angular/common/http';
import { finalize, map } from 'rxjs/operators';
import { TipoTransporteOferta } from '../../entities/enumerations/tipo-transporte-oferta.model';
import { TipoCarga } from '../../entities/enumerations/tipo-carga.model';
import { AppGoogleService } from '../../core/app/app.google.service';
import { StatusOferta } from '../../entities/enumerations/status-oferta.model';
@Component({
  selector: 'jhi-oferta-divulgar',
  templateUrl: './oferta-divulgar.component.html',
  styleUrls: ['./oferta-divulgar.component.scss'],
})
export class OfertaDivulgarComponent implements OnInit {
  isSaving = false;
  ofertas: IOfertas | null = null;
  editOrigem = false;
  editDestino = false;

  activeIndex: number = 0;

  oldOrigem = null;
  oldDestino = null;

  tiposTransportes = [
    { name: 'Cegonha', value: TipoTransporteOferta.CEGONHA },
    { name: 'Guincho', value: TipoTransporteOferta.GUINCHO },
    { name: 'Reboque', value: TipoTransporteOferta.RAMPA },
  ];

  status = [
    { name: 'Atendida', value: StatusOferta.ATENDIDA_INFOCARGAS },
    { name: 'Aguardando Proposta', value: StatusOferta.AGUARDANDO_PROPOSTA },
    { name: 'Cancelada', value: StatusOferta.CANELADA },
  ];

  tiposCargas = [
    { name: 'Moto', value: TipoCarga.MOTO },
    { name: 'Carro', value: TipoCarga.CARRO },
    { name: 'Caminhão', value: TipoCarga.CAMINHAO },
  ];

  filteredOrigem: any | null | undefined;
  filteredDestino: any | null | undefined;

  perfilsSharedCollection: IPerfil[] = [];

  editForm: OfertasFormGroup = this.ofertasFormService.createOfertasPortalFormGroup();

  constructor(
    protected dataUtils: DataUtils,
    protected ofertasService: OfertasService,
    protected ofertasFormService: OfertasFormService,
    protected perfilService: PerfilService,
    protected activatedRoute: ActivatedRoute,
    protected appGoogleService: AppGoogleService
  ) {}

  comparePerfil = (o1: IPerfil | null, o2: IPerfil | null): boolean => this.perfilService.comparePerfil(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ofertas }) => {
      this.ofertas = ofertas;
      if (ofertas) {
        if (this.ofertas?.id === null) {
          this.editDestino = true;
          this.editOrigem = true;
        } else {
          this.oldDestino = ofertas.localizacaoDestino;
          this.oldOrigem = ofertas.localizacaoOrigem;
        }
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

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;

    const ofertas = this.ofertasFormService.getOfertas(this.editForm);

    let selectOrigem = this.editForm.get(['localizacaoOrigem'])?.value;

    console.log(selectOrigem);

    if (selectOrigem == undefined || null) {
      selectOrigem = this.oldOrigem;
    }

    if (ofertas.id !== null && !this.editOrigem) {
      selectOrigem = JSON.parse(selectOrigem);
    }

    ofertas.origem = `${selectOrigem['structured_formatting']['main_text']} - ${selectOrigem['structured_formatting']['secondary_text']}`;
    ofertas.localizacaoOrigem = JSON.stringify(selectOrigem);

    let selectDestino = this.editForm.get(['localizacaoDestino'])?.value;

    if (selectDestino == undefined || null) {
      selectDestino = this.oldDestino;
    }

    if (ofertas.id !== null && !this.editDestino) {
      selectDestino = JSON.parse(selectDestino);
    }

    ofertas.destino = `${selectDestino['structured_formatting']['main_text']} - ${selectDestino['structured_formatting']['secondary_text']}`;
    ofertas.localizacaoDestino = JSON.stringify(selectDestino);

    if (ofertas.id !== null) {
      this.subscribeToSaveResponse(this.ofertasService.updateOferta(ofertas));
    } else {
      this.subscribeToSaveResponse(this.ofertasService.createOferta(ofertas));
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

  filtrarOrigem(event: any, ehDestino = false): void {
    let query = event.query;
    this.appGoogleService.autocomplete(query).subscribe(res => {
      if (ehDestino) {
        this.filteredDestino = res.body;
      } else {
        this.filteredOrigem = res.body;
      }
    });
  }

  setDestino() {
    this.editDestino = !this.editDestino;
  }

  setOrigem() {
    this.editOrigem = !this.editOrigem;
  }
}
