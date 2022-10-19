import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISolicitacao } from '../solicitacao.model';

@Component({
  selector: 'jhi-solicitacao-detail',
  templateUrl: './solicitacao-detail.component.html',
})
export class SolicitacaoDetailComponent implements OnInit {
  solicitacao: ISolicitacao | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ solicitacao }) => {
      this.solicitacao = solicitacao;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
