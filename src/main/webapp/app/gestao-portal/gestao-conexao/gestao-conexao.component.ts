import { Component, OnInit } from '@angular/core';
import { Account } from '../../core/auth/account.model';
import { AppService } from '../../core/app/app.service';
import { ActivatedRoute } from '@angular/router';
import { IOfertas } from '../../entities/ofertas/ofertas.model';

@Component({
  selector: 'jhi-gestao-conexao',
  templateUrl: './gestao-conexao.component.html',
  styleUrls: ['./gestao-conexao.component.scss'],
})
export class GestaoConexaoComponent implements OnInit {
  account: Account | null | undefined = null;
  visible: boolean = false;

  ofertas: IOfertas | null = null;

  home = { icon: 'pi pi-home', routerLink: '/gestao' };
  items = [{ label: 'Divulgar Vagas/Cargas' }, { label: 'Escolher Carga' }];

  constructor(private appService: AppService, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.account = this.appService.account;
    this.activatedRoute.data.subscribe(({ ofertas }) => {
      this.ofertas = ofertas;
    });
  }
}
