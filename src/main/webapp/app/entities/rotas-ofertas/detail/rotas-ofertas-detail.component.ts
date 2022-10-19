import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRotasOfertas } from '../rotas-ofertas.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-rotas-ofertas-detail',
  templateUrl: './rotas-ofertas-detail.component.html',
})
export class RotasOfertasDetailComponent implements OnInit {
  rotasOfertas: IRotasOfertas | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ rotasOfertas }) => {
      this.rotasOfertas = rotasOfertas;
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
}
