import { Component, OnInit } from '@angular/core';
import { TipoConta } from '../../entities/enumerations/tipo-conta.model';

@Component({
  selector: 'jhi-criar-perfil',
  templateUrl: './criar-perfil.component.html',
  styleUrls: ['./criar-perfil.component.scss'],
})
export class CriarPerfilComponent implements OnInit {
  tipoConta: TipoConta | undefined;

  constructor() {}

  ngOnInit(): void {}

  setOptions(tipoConta: any): void {
    this.tipoConta = tipoConta;
  }
}
