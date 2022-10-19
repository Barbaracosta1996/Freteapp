import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IOfertas } from '../ofertas.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-ofertas-detail',
  templateUrl: './ofertas-detail.component.html',
})
export class OfertasDetailComponent implements OnInit {
  ofertas: IOfertas | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ofertas }) => {
      this.ofertas = ofertas;
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
