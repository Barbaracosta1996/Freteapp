import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFrota } from '../frota.model';

@Component({
  selector: 'jhi-frota-detail',
  templateUrl: './frota-detail.component.html',
})
export class FrotaDetailComponent implements OnInit {
  frota: IFrota | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ frota }) => {
      this.frota = frota;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
